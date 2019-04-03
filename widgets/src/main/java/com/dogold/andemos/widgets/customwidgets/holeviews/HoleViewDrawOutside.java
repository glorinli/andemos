package com.dogold.andemos.widgets.customwidgets.holeviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * HoleViewDrawOutside
 * Created by glorin on 5/7/17.
 */

public class HoleViewDrawOutside extends View {
    private Paint mPaint;
    private RectF mCircleRect;
    private Path mOutsidePath;
    private int mOutsideColor = 0xcc000000;

    public HoleViewDrawOutside(Context context) {
        super(context);
        initView();
    }

    public HoleViewDrawOutside(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HoleViewDrawOutside(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleRect = new RectF();
        mOutsidePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0 && h > 0) {
            float radius = Math.min(w, h) / 2;

            float centerX = w / 2;
            float centerY = h / 2;

            mCircleRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

            mOutsidePath.reset();

            mOutsidePath.moveTo(centerX, 0);
            mOutsidePath.lineTo(centerX, centerY);
            mOutsidePath.addArc(mCircleRect, 270, 360);
            mOutsidePath.lineTo(centerX, 0);
            mOutsidePath.lineTo(0, 0);
            mOutsidePath.lineTo(0, h);
            mOutsidePath.lineTo(w, h);
            mOutsidePath.lineTo(w, 0);
            mOutsidePath.lineTo(centerX, 0);
            mOutsidePath.close();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mOutsideColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mOutsidePath, mPaint);
    }
}
