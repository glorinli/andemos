package com.dogold.andemos.touchevents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent

class MainActivity : AppCompatActivity() {
    private val TAG = "Touch.MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent begin ${getActionName(event?.action)}")
        val handled = super.onTouchEvent(event)
        Log.d(TAG, "onTouchEvent ${getActionName(event?.action)}, result: $handled")
        return handled
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "dispatchTouchEvent begin ${getActionName(ev?.action)}")
        val handled = super.dispatchTouchEvent(ev)
        Log.d(TAG, "dispatchTouchEvent ${getActionName(ev?.action)}, result: $handled")
        return handled
    }
}
