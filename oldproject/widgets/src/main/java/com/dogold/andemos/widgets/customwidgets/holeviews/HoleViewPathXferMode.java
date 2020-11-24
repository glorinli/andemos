package com.dogold.andemos.widgets.customwidgets.holeviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * HoleViewDrawOutside
 * Created by glorin on 5/7/17.
 */

public class HoleViewPathXferMode extends View {
    private Paint mPaint;
    private RectF mCircleRect;
    private int mOutsideColor = 0xcc000000;
    private Path mFillPath;
    private Path mCirclePath;
    private Xfermode mClearMode;

    public HoleViewPathXferMode(Context context) {
        super(context);
        initView();
    }

    public HoleViewPathXferMode(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HoleViewPathXferMode(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleRect = new RectF();
        mCirclePath = new Path();
        mFillPath = new Path();
        mClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        setLayerType(View.LAYER_TYPE_HARDWARE, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0 && h > 0) {
            float radius = Math.min(w, h) / 2;

            float centerX = w / 2;
            float centerY = h / 2;

            mCircleRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

            mCirclePath.reset();
            mCirclePath.addOval(mCircleRect, Path.Direction.CW);
            mCirclePath.close();

            mFillPath.reset();
            mFillPath.addRect(0, 0, w, h, Path.Direction.CW);
            mFillPath.close();

            if (!isInEditMode())
                mFillPath.op(mCirclePath, Path.Op.DIFFERENCE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(mOutsideColor);

        mPaint.setXfermode(mClearMode);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mCirclePath, mPaint);
    }
}
