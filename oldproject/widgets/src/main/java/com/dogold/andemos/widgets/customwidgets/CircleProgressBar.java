package com.dogold.andemos.widgets.customwidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dogold.andemos.widgets.R;


/**
 * A circle progress bar
 * Created by glorin on 7/22/16.
 */
public class CircleProgressBar extends View {
    private static final int DEFAULT_MAX_PROGRESS = 100;
    private static final int DEFAULT_STROKE_WIDTH = 5;

    private static final boolean ANIMATE_PROGRESS = true;
    private static final long ANIMATE_DURATION = 300L;

    private static final boolean DEBUG = true;

    private static final String TAG = "CircleProgressBar";

    public CircleProgressBar(Context context) {
        super(context);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAttrs = attrs;
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrs = attrs;
        init();
    }

    protected int mMaxProgress = DEFAULT_MAX_PROGRESS;
    private int mProgress;
    private float mSpeed; // Incremental in 1ms
    private float mCurrentProgress;   // Used when animate progress change
    private long mLastComputeTime;
    private AttributeSet mAttrs;

    private int mForeColor;
    private int mBackColor;

    private int mStrokeWidth;
    private RectF mViewRectF;

    private Paint mPaint;

    private void init() {
        mViewRectF = new RectF();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mForeColor = 0xff17cdc7;
        mBackColor = 0x88000000;

        mStrokeWidth = DEFAULT_STROKE_WIDTH;

        if (mAttrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(mAttrs, R.styleable.CircleProgressBar);

            mStrokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_cpb_strokeWidth, mStrokeWidth);

            mForeColor = a.getColor(R.styleable.CircleProgressBar_cpb_strokeColor, mForeColor);

            mBackColor = a.getColor(R.styleable.CircleProgressBar_cpb_circleBackgroundColor, mBackColor);

            a.recycle();
        }

        mPaint.setStrokeWidth(mStrokeWidth);

        mProgress = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateViewRectF();
    }

    private void updateViewRectF() {
        mViewRectF.set(mStrokeWidth / 2, mStrokeWidth / 2,
                getWidth() - mStrokeWidth / 2, getHeight() - mStrokeWidth / 2);
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        mPaint.setStrokeWidth(mStrokeWidth);
        updateViewRectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw background oval
        mPaint.setColor(mBackColor);
        canvas.drawOval(mViewRectF, mPaint);

        mPaint.setColor(mForeColor);
        canvas.drawArc(mViewRectF, 270, 360 * mCurrentProgress / mMaxProgress, false, mPaint);

        if (mCurrentProgress < mProgress) {
            postInvalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        long currentTime = SystemClock.uptimeMillis();

        if (ANIMATE_PROGRESS && mCurrentProgress < mProgress && mLastComputeTime > 0L) {
            mCurrentProgress += mSpeed * (currentTime - mLastComputeTime);

            mCurrentProgress = Math.min(mCurrentProgress, mProgress);

            invalidate();
        }

        mLastComputeTime = currentTime;

//        if (DEBUG) Log.d(TAG, "computeScroll, current progress: " + mCurrentProgress);
    }

    public void setMaxProgress(int max) {
        if (max <= 1) {
            throw new IllegalArgumentException("Max progress should be greater than 1");
        }
        mMaxProgress = max;

        postInvalidate();
    }

    public void setProgress(int progress) {
        if (ANIMATE_PROGRESS) {
            mProgress = Math.min(mMaxProgress, Math.max(progress, 0));

            if (mCurrentProgress > mProgress) {
                mCurrentProgress = mProgress;
            }

            mSpeed = (mProgress - mCurrentProgress) / ANIMATE_DURATION;

            if (DEBUG) Log.d(TAG, "setProgress: " + progress + ", speed: " + mSpeed);

            mLastComputeTime = 0L;
            postInvalidate();
        } else {
            mCurrentProgress = mProgress = Math.min(mMaxProgress, Math.max(progress, 0));
        }

        postInvalidate();
    }

    public void setForeColor(int foreColor) {
        mForeColor = foreColor;
        postInvalidate();
    }

    public void setBackColor(int backColor) {
        mBackColor = backColor;
        postInvalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }
}
