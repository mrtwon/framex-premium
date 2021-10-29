package com.mrtwon.framex_premium.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mrtwon.framex_premium.R
import java.util.*

fun createNotification(message: String, title: String, channelName: String, pendingIntent: PendingIntent? = null, context: Context) {
    createNotificationChannel(context, channelName)
    val id = Random().nextInt(10000)
    val builder = NotificationCompat.Builder(context, "101")
        .setSmallIcon(R.mipmap.icon_app)
        .setContentTitle("Новая серия")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setAutoCancel(true)
    if(pendingIntent != null)
        builder.setContentIntent(pendingIntent)
    val manager = NotificationManagerCompat.from(context)
    manager.notify(id, builder.build())
}

fun createNotificationChannel(context: Context, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel("101", channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}