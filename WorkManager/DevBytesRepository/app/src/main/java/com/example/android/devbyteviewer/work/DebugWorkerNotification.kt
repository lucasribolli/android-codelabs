package com.example.android.devbyteviewer.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.android.devbyteviewer.R

class DebugWorkerNotification(private val context: Context) {
    companion object {
        private const val id = 1
        private const val channelId = "channel.debug.daily.worker"
    }

    private val manager = NotificationManagerCompat.from(context)

    init {
        createChannel()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "WorkRequest Debug",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    fun show() {
        val notification = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_debug)
            setContentTitle("WorkManagerDebug")
            setContentText("Executed now")
        }

        manager.notify(id, notification.build())
    }
}