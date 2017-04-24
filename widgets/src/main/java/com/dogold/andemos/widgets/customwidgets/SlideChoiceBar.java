package com.dogold.andemos.widgets.customwidgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.dogold.andemos.widgets.R;
import com.dogold.andemos.widgets.utils.EvaluateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SlideChoiceBar
 * Created by glorin on 24/04/2017.
 */

public class SlideChoiceBar extends View {
    private static final String TAG = "SlideChoiceBar";
    private List<Entry> mEntries;
    private Paint mPaint;
    private AttributeSet mAttrs;
    private int mTextSize;
    private int mActiveColor, mInactiveColor;
    private float mEntrySelectedScale = 1.5f;
    private int mSlideBarHeight;
    private int mSlideIndicatorSize;
    private int mIndicatorOffset;
    private Rect mIndicatorBound = new Rect();
    private int mIndicatorLeftMax, mIndicatorRightMax;
    private int mEntryGap;

    private Drawable mSlideBarBackground, mSlideBarIndicator;

    private GestureDetector mGestureDetector;

    private int mCurrentSelectedIndex;

    private Scroller mScroller;

    public SlideChoiceBar(Context context) {
        super(context);
        initViews();
    }

    public SlideChoiceBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAttrs = attrs;
        initViews();
    }

    public SlideChoiceBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrs = attrs;
        initViews();
    }

    private void initViews() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mScroller = new Scroller(getContext());

        if (mAttrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(mAttrs, R.styleable.SlideChoiceBar);

            Resources resources = getResources();
            Resources.Theme theme = getContext().getTheme();

            mActiveColor = a.getColor(R.styleable.SlideChoiceBar_activeColor, Color.WHITE);
            mInactiveColor = a.getColor(R.styleable.SlideChoiceBar_inactiveColor, Color.GRAY);
            mTextSize = a.getDimensionPixelSize(R.styleable.SlideChoiceBar_entryTextSize, resources.getDimensionPixelSize(R.dimen.scb_textSize));

            if (mTextSize > 0) {
                mPaint.setTextSize(mTextSize);
            }

            mSlideBarHeight = a.getDimensionPixelSize(R.styleable.SlideChoiceBar_barHeight, resources.getDimensionPixelSize(R.dimen.scb_barHeight));
            mSlideIndicatorSize = a.getDimensionPixelSize(R.styleable.SlideChoiceBar_indicatorSize, resources.getDimensionPixelSize(R.dimen.scb_indicatorSize));

            mSlideBarBackground = a.getDrawable(R.styleable.SlideChoiceBar_barBackground);
            if (mSlideBarBackground == null) {
                mSlideBarBackground = resources.getDrawable(R.drawable.scb_bar_background, theme);
            }

            mSlideBarIndicator = a.getDrawable(R.styleable.SlideChoiceBar_indicator);
            if (mSlideBarIndicator == null) {
                mSlideBarIndicator = resources.getDrawable(R.drawable.scb_bar_indicator, theme);
            }

            int entryValueArrayResId = a.getResourceId(R.styleable.SlideChoiceBar_entries, 0);
            if (entryValueArrayResId > 0) {
                String[] stringArray = resources.getStringArray(entryValueArrayResId);
                if (stringArray.length > 0) {
                    mEntries = new ArrayList<>();
                    for (String s : stringArray) {
                        Entry ent = new Entry();
                        ent.text = s;
                        ent.textWidth = mPaint.measureText(ent.text);
                        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
                        ent.textHeight = fmi.descent - fmi.ascent;
                        ent.scale = 1f;

                        mEntries.add(ent);
                    }
                } else {
                    throw new IllegalArgumentException("At least one entry should be provided");
                }
            } else {
                throw new IllegalArgumentException("Entrys must be provided");
            }

            a.recycle();
        }

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            private boolean mDragging;

            @Override
            public boolean onDown(MotionEvent e) {
                float y = e.getY();
                float x = e.getX();
                int halfIndicatorSize = mSlideIndicatorSize / 2;
                if (y >= mIndicatorBound.top && y <= mIndicatorBound.bottom && x >= mIndicatorLeftMax - halfIndicatorSize && x <= mIndicatorRightMax + halfIndicatorSize) {
                    mDragging = true;
                    moveIndicator(EvaluateUtils.between(mIndicatorLeftMax, mIndicatorRightMax, x));
                } else {
                    mDragging = false;
                }

                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (e1 == null || e2 == null) {
                    return false;
                }

                if (mDragging) {
                    float x = e2.getX();
                    if (x >= mIndicatorLeftMax && x <= mIndicatorRightMax) {
                        moveIndicator(x);
                    }
                }

                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    private void moveIndicator(float x) {
        mIndicatorBound.offsetTo(Math.round(x - mIndicatorBound.width() / 2), mIndicatorBound.top);
        mIndicatorOffset = mIndicatorBound.centerX() - mIndicatorLeftMax;
        mSlideBarIndicator.setBounds(mIndicatorBound);

        updateEntryScale();

        invalidate();
    }

    private void updateEntryScale() {
        // Update entry scale
        float currentPosition = (float) mIndicatorOffset / mEntryGap;

        for (int i = 0; i < mEntries.size(); i++) {
            float distance = Math.abs(currentPosition - i);
            float fraction = EvaluateUtils.between(0, 1, distance);
            float scale = EvaluateUtils.evaluateFloat(mEntrySelectedScale, 1, fraction); // The farther, the smaller

            Entry entry = mEntries.get(i);

            entry.scale = Math.min(mEntrySelectedScale, Math.max(1, scale));

            entry.textColor = EvaluateUtils.evaluateColor(fraction, mActiveColor, mInactiveColor);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_CANCEL) {
            // Scroll to target
            int targetIndex = Math.round((float) mIndicatorOffset / mEntryGap);
            int targetX = mIndicatorLeftMax + mEntryGap * targetIndex;

            Log.d(TAG, "onUp, target: " + targetIndex);

            mScroller.startScroll(mIndicatorOffset, 0, targetX - mIndicatorOffset, 0);

            postInvalidate();
        }

        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) {
            moveIndicator(mScroller.getCurrX());
            postInvalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0 && h > 0) {
            updatePositions(w, h);
            updateEntryScale();
        }
    }

    private void updatePositions(int width, int height) {
        int halfIndicatorSize = mSlideIndicatorSize / 2;

        mIndicatorLeftMax = (int) (getPaddingLeft() + Math.max(halfIndicatorSize, mEntries.get(0).textWidth * mEntrySelectedScale / 2));
        mIndicatorRightMax = (int) (width - getPaddingRight() - Math.max(halfIndicatorSize, mEntries.get(mEntries.size() - 1).textWidth * mEntrySelectedScale / 2));

        // Update bar background
        int barBackgroundTop = height - getPaddingBottom() - halfIndicatorSize - mSlideBarHeight / 2;
        if (mSlideBarBackground != null) {
            mSlideBarBackground.setBounds(mIndicatorLeftMax, barBackgroundTop, mIndicatorRightMax, barBackgroundTop + mSlideBarHeight);
        }

        // Updata bar indicator position
        int indicatorTop = height - getPaddingBottom() - mSlideIndicatorSize;
        int indicatorCenter = Math.min(mIndicatorLeftMax + mIndicatorOffset, mIndicatorRightMax);

        mIndicatorBound.set(indicatorCenter - halfIndicatorSize, indicatorTop, indicatorCenter + halfIndicatorSize, indicatorTop + mSlideIndicatorSize);
        if (mSlideBarIndicator != null) {
            mSlideBarIndicator.setBounds(mIndicatorBound);
        }

        // Update text bounds
        mEntryGap = (mIndicatorRightMax - mIndicatorLeftMax) / (mEntries.size() - 1);
        int entryCenterY = (int) (getPaddingTop() + mEntries.get(0).textHeight * mEntrySelectedScale / 2 + 1);
        for (int i = 0; i < mEntries.size(); i++) {
            Entry entry = mEntries.get(i);
            int entryCenter = mIndicatorLeftMax + mEntryGap * i;
            if (entry.bound == null) {
                entry.bound = new RectF();
            }

            entry.bound.set(entryCenter - entry.textWidth / 2, entryCenterY - entry.textHeight / 2,
                    entryCenter + entry.textWidth / 2, entryCenterY + entry.textHeight / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw bar background
        if (mSlideBarBackground != null) {
            mSlideBarBackground.draw(canvas);
        }

        // Draw indicator
        if (mSlideBarIndicator != null) {
            mSlideBarIndicator.draw(canvas);
        }

        // Draw entries
        for (Entry e : mEntries) {
            RectF r = e.bound;
            mPaint.setColor(e.textColor);
            mPaint.setTextSize(mTextSize * e.scale);
            canvas.drawText(e.text, r.centerX(), r.bottom, mPaint);
        }
    }

    private static class Entry {
        String text;
        RectF bound;
        float textWidth;
        float textHeight;
        float scale;
        int textColor;
    }
}
