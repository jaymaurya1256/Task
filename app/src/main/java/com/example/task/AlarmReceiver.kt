package com.example.task

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService

private const val TAG = "AlarmReceiver"
private const val CHANNEL_ID = "Alarm Notification"

class AlarmReceiver(): BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "RemindingAlarm"){
            val notification = NotificationCompat.Builder(p0!!, CHANNEL_ID )
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Reminder")
                .setContentText(p0.getString(R.string.notification))
                .build()

            val notificationManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(createNotificationChannel() as NotificationChannel)
                notificationManager.notify(666,notification)
            }

        }
    }

    private fun createNotificationChannel(): Any {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
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