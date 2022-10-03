package com.trunnghieu.tplogisticsapplication.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*

/**
 * Created by thongphm on 2020-01-30
 */
object NotificationUtils {

    val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        PendingIntent.FLAG_IMMUTABLE
    else
        PendingIntent.FLAG_UPDATE_CURRENT

    /**
     * Create a notification channel for Android O and later
     *
     * @param context     - Application context
     * @param channelId   - ID of notification
     * @param channelName - Name of notification channel
     */
    fun createNotificationChannel(
        context: Context, channelId: String?, channelName: String?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(true)
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            Objects.requireNonNull(manager).createNotificationChannel(serviceChannel)
        }
    }

    /**
     * Build new notification
     *
     * @param context          - Application context
     * @param channelId        - ID of notification channel
     * @param channelName      - Name of notification channel
     * @param notificationIcon - Notification icon
     * @return NotificationCompat.Builder
     */
    fun buildNotification(
        context: Context?,
        channelId: String?,
        channelName: String?,
        notificationIcon: Int,
        contentTitle: String?,
        contentText: String?,
        priority: Int,
        badgeCount: Int? = 0,
        pendingIntent: PendingIntent?
    ): Notification {
        return NotificationCompat.Builder(context!!, channelName!!)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setSmallIcon(notificationIcon)
            .setColor(Color.parseColor("#083A75"))
            .setChannelId(channelId!!)
            .setContentTitle(contentTitle)
            .setTicker(contentTitle)
            .setContentText(contentText)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setNumber(badgeCount ?: 0)
            .build()
    }
}