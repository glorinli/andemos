package xyz.dogold.andemos.sensors.gyro;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * GyroHorizontalScrollView
 * Created by glorin on 13/07/2017.
 */

public class GyroHorizontalScrollView extends HorizontalScrollView implements SensorEventListener {
    private static final String TAG = "GyroHSV";
    private SensorManager mSensorManager;
    private boolean mListeningGyro;
    private float mScrollPercentPerDegree = 100 / 30f;
    private int mScrollPixelsPerDegree;
    private long mLastSensorEventTime;

    public GyroHorizontalScrollView(Context context) {
        this(context, null);
    }

    public GyroHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GyroHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) return;

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
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

        if (w > 0) {
            mScrollPixelsPerDegree = (int) (mScrollPercentPerDegree * w / 100);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float angleSpeedX = (float) Math.toDegrees(sensorEvent.values[0]);
            float angleSpeedY = (float) Math.toDegrees(sensorEvent.values[1]);
            float angleSpeedZ = (float) Math.toDegrees(sensorEvent.values[2]);

//            Log.d(TAG, String.format("Gyro data:\nX: %1$f\nY: %2$f\nZ: %3$f", angleSpeedX, angleSpeedY, angleSpeedZ));

            long currentTime = SystemClock.uptimeMillis();

            if (mLastSensorEventTime != 0) {
                long duration = currentTime - mLastSensorEventTime;

                float angles = angleSpeedY * duration / 1000;

                Log.d(TAG, "Rotate angles: " + angles);

                int scrollOffset = (int) (mScrollPixelsPerDegree * angles);

                if (Math.abs(angles) > 0.3f) {
                    smoothScrollBy(scrollOffset, 0);
                }
            }

            mLastSensorEventTime = currentTime;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
