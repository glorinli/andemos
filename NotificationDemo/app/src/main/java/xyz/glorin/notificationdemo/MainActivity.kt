package xyz.glorin.notificationdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var tvTestBtnClickedTime: TextView
    private var clickedTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationHelper = NotificationHelper(this)

        findViewById<View>(R.id.btnNotificationWithColoredAction).setOnClickListener {
            notificationHelper.sendNotificationWithColoredAction()
        }

        tvTestBtnClickedTime = findViewById(R.id.tvTestButtonClickedTime)
        findViewById<Button>(R.id.btnTest).setOnClickListener {
            tvTestBtnClickedTime.text = "Clicked ${clickedTime++} times"
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        Log.d(TAG, "onAttachedToWindow")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        Log.d(TAG, "onDetachedFromWindow")
    }

    companion object {
        private const val TAG = "MainActivity_TAG"
    }
}