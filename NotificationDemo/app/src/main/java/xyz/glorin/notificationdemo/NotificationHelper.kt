package xyz.glorin.notificationdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationHelper(private val context: Context) {
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                "ChannelId",
                "Bubble notification channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                notificationManager.createNotificationChannel(this)
            }
        }
    }

    fun sendNotificationWithColoredAction() {
//        val actionText = SpannableStringBuilder("Action").apply {
//            setSpan(ForegroundColorSpan(Color.RED), 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        }
        val actionText = "Action"

        NotificationCompat.Builder(context, "ChannelId")
            .setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
            .setSmallIcon(R.drawable.ic_notification_join_now)
            .setContentTitle("Demo Notification")
            .setContentText("This is a demo notification")
            .setAutoCancel(true)
            .setContentIntent(null)
            .setColor(Color.RED)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setShowWhen(true)
            .setWhen(System.currentTimeMillis())
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.ic_notification_action_join_tint,
                    actionText, null
                )
                    .build()
            )
            .build().apply {
                notificationManager.notify(100, this)
            }
    }
}