package xyz.dogold.andemos.sensors.gyro;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * GyroHorizontalScrollView
 * Created by glorin on 13/07/2017.
 */

public class GyroScrollView extends FrameLayout implements SensorEventListener {
    private static final String TAG = "GyroScrollView";
    private SensorManager mSensorManager;
    private boolean mListeningGyro;
    private float mScrollPercentPerDegree = 100 / 60f;
    private int mScrollPixelsPerDegreeHorizontal, mScrollPixelPerDegreeVertical;

    private float mScrollSpeedHorizontal, mScrollSpeedVertical; // Pixels / Second
    private long mLastScrollTime;

    private boolean mHasScrollToCenterWhenInit;
    private int mMaxScrollX, mMaxScrollY;
    private boolean mCanScrollHorizontal, mCanScrollVertical;

    public GyroScrollView(Context context) {
        this(context, null);
    }

    public GyroScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GyroScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) return;

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final View childAt = getChildAt(0);

        if (childAt == null) {
            Log.e(TAG, "No children found");
            return;
        }

        childAt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int childWidth = childAt.getWidth();
                int childHeight = childAt.getHeight();

                mCanScrollHorizontal = childWidth > getWidth();
                mCanScrollVertical = childHeight > getHeight();

                mMaxScrollX = childWidth - getWidth();
                mMaxScrollY = childHeight - getHeight();

                if (childWidth > 0) {
                    mScrollPixelsPerDegreeHorizontal = (int) (mScrollPercentPerDegree * childWidth / 100);
                }

                if (childHeight > 0) {
                    mScrollPixelPerDegreeVertical = (int) (mScrollPercentPerDegree * childHeight / 100);
                }

                if (!mHasScrollToCenterWhenInit) {
                    scrollToCenter();
                    mHasScrollToCenterWhenInit = true;
                }
            }
        });
    }

    public void scrollToCenter() {
        // Scroll to center
        View child = getChildAt(0);
        if (child != null) {
            scrollTo(child.getWidth() / 2, 0);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) return;

        if (getVisibility() == VISIBLE && !mListeningGyro) {
            startListenGyro();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (isInEditMode()) return;

        if (mListeningGyro) stopListenGyro();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (isInEditMode()) return;

        if (!isAttachedToWindow()) return;

        if (visibility == VISIBLE) {
            if (!mListeningGyro) startListenGyro();
        } else {
            if (mListeningGyro) stopListenGyro();
        }
    }

    private void startListenGyro() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
            Log.e(TAG, "Gyro sensor not supported!!!");
            return;
        }

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        mListeningGyro = true;
    }

    private void stopListenGyro() {
        mSensorManager.unregisterListener(this);
        mListeningGyro = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float angleSpeedX = (float) Math.toDegrees(sensorEvent.values[0]);
            float angleSpeedY = (float) Math.toDegrees(sensorEvent.values[1]);
            float angleSpeedZ = (float) Math.toDegrees(sensorEvent.values[2]);

//            Log.d(TAG, String.format("Gyro data:\nX: %1$f\nY: %2$f\nZ: %3$f", angleSpeedX, angleSpeedY, angleSpeedZ));

            mScrollSpeedHorizontal = angleSpeedY * mScrollPixelsPerDegreeHorizontal;
            mScrollSpeedVertical = angleSpeedX * mScrollPixelPerDegreeVertical;
            postInvalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // noops
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

//        Log.d(TAG, "computeScroll, speed: " + mScrollSpeedHorizontal + ", " + mScrollSpeedVertical);

        long currentTime = SystemClock.uptimeMillis();

        if (mLastScrollTime > 0) {
            long duration = currentTime - mLastScrollTime;

            boolean shouldInvalidate = false;

            if (mCanScrollHorizontal && Math.abs(mScrollSpeedHorizontal) > 10) {
                safeScrollBy(-(int) (mScrollSpeedHorizontal * duration / 1000), 0);
                shouldInvalidate = true;
            }

            if (mCanScrollVertical && Math.abs(mScrollSpeedVertical) > 10) {
                safeScrollBy(0, -(int) (mScrollSpeedVertical * duration / 1000));
                shouldInvalidate = true;
            }

            if (shouldInvalidate) {
                invalidate();
            }
        }

        mLastScrollTime = currentTime;
    }

    private void safeScrollBy(int dx, int dy) {
        scrollTo(Math.max(0, Math.min(mMaxScrollX, getScrollX() + dx)), Math.max(0, Math.min(mMaxScrollY, getScrollY() + dy)));
    }
}
