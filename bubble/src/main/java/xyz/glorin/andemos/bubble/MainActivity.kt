package xyz.glorin.andemos.bubble

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Person
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnSendNotification).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                sendNotificationWithBubble()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun sendNotificationWithBubble() {
        val target = Intent(this, BubbleActivity::class.java)
        val bubbleIntent = PendingIntent.getActivity(this, 0, target, 0)

        val bubbleData = Notification.BubbleMetadata.Builder(
            bubbleIntent,
            Icon.createWithResource(this, R.drawable.ic_launcher_foreground)
        ).setDesiredHeight(600).build()

        val chatPartner =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Person.Builder()
                    .setName("NNN")
                    .setImportant(true)
                    .build()
            } else {
                null
            }

        val notification = Notification.Builder(this, "BubbleChannel")
            .setContentIntent(bubbleIntent)
            .setSmallIcon(Icon.createWithResource(this, R.drawable.ic_launcher_foreground))
            .setBubbleMetadata(bubbleData)
            .addPerson(chatPartner)
            .build()

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(100, notification)
    }
}