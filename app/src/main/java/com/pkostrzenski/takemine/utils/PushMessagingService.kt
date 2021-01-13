package com.pkostrzenski.takemine.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.product.ProductActivity

class PushMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if(remoteMessage.data.isNotEmpty())
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

        if(remoteMessage.notification != null){
            Log.d(TAG, "Message Notification data payload: " + remoteMessage.notification)

            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body
            val productId = remoteMessage.data[PRODUCT_ID_KEY]

            val elements = listOf(title, body, productId)
            if(elements.all { it != null }){ // nothing is null
                sendNotification(title!!, body!!, productId!!)
            }
        }
    }

    private fun sendNotification(title: String, body: String, eventId: String){
        val notificationIntent = Intent(this, ProductActivity::class.java)
        notificationIntent.putExtra(PRODUCT_ID_KEY, eventId)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val contentIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.obtain)
            .setSound(defaultSoundUri)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Event Push",
                NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(0, notification.build())
    }

    companion object{
        const val PRODUCT_ID_KEY = "product_id"
        const val TAG = "FIREBASE_MESSAGING"
    }
}