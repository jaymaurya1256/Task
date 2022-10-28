package com.example.task

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

private const val TAG = "AlarmReceiver"
private const val CHANNEL_ID = "Alarm Notification"

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Reminder")
                .setContentText(p1?.action ?: "You have a task to complete!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager
            } else {
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(createNotificationChannel() as NotificationChannel)
                notificationManager.notify(666, notification)
            }

    }

    private fun createNotificationChannel(): Any {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = "You have a pending task to do now"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val name = "Notification"
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = descriptionText
            }
            return channel
        }
        return true
    }

}