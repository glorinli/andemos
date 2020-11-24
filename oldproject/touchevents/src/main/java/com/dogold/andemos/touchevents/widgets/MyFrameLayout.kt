package com.dogold.andemos.touchevents.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import com.dogold.andemos.touchevents.getActionName

class MyFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val TAG = "Touch.MyFrameLayout"

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.i(TAG, "onInterceptTouchEvent begin ${getActionName(ev?.action)}")
        val intercepted = super.onInterceptTouchEvent(ev)
        Log.i(TAG, "onInterceptTouchEvent ${getActionName(ev?.action)}, result: $intercepted")
        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i(TAG, "onTouchEvent begin ${getActionName(event?.action)}")
        val handled = super.onTouchEvent(event)
        Log.i(TAG, "onTouchEvent ${getActionName(event?.action)}, result: $handled")
        return handled
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.i(TAG, "dispatchTouchEvent begin ${getActionName(ev?.action)}")
        val handled = super.dispatchTouchEvent(ev)
        Log.i(TAG, "dispatchTouchEvent ${getActionName(ev?.action)}, result: $handled")
        return handled
    }
}