package com.dogold.andemos.touchevents.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.dogold.andemos.touchevents.getActionName

class MyView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val TAG = "Touch.MyView"

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e(TAG, "onTouchEvent begin ${getActionName(event?.action)}")
        val handled = super.onTouchEvent(event)
        Log.e(TAG, "onTouchEvent ${getActionName(event?.action)}, result: $handled")
        return handled
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e(TAG, "dispatchTouchEvent begin ${getActionName(ev?.action)}")
        val handled = super.dispatchTouchEvent(ev)
        Log.e(TAG, "dispatchTouchEvent ${getActionName(ev?.action)}, result: $handled")
        return handled
    }
}