package com.appyhigh.mylibrary.push

import android.app.*
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.appyhigh.mylibrary.misc.Constants.FCM_ICON
import com.appyhigh.mylibrary.misc.Constants.FCM_TARGET_ACTIVITY
import com.appyhigh.mylibrary.misc.getBitmapfromUrl
import com.clevertap.android.sdk.CleverTapAPI
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessaging: FirebaseMessagingService() {
    var bitmap: Bitmap? = null

    override fun onMessageSent(s: String) {
        super.onMessageSent(s)
        Log.d(TAG, "onMessageSent: $s")
    }

    override fun onSendError(s: String, e: Exception) {
        super.onSendError(s, e)
        Log.d(TAG, "onSendError: $e")
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        CleverTapAPI.getDefaultInstance(applicationContext)?.pushFcmRegistrationId(s, true)
        Log.d(TAG, "onNewToken: $s")
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            // There are two types of messages data messages and notification messages. Data messages are handled
            // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
            // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
            // is in the foreground. When the app is in the background an automatically generated notification is displayed.
            // When the user taps on the notification they are returned to the app. Messages containing both notification
            // and data payloads are treated as notification messages. The Firebase console always sends notification
            // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
            Log.d(
                TAG,
                "From: " + remoteMessage.from
            )
            // Check if message contains a data payload.
            if (remoteMessage.data.isNotEmpty()) {
                Log.d(
                    TAG,
                    "Message data payload: " + remoteMessage.data
                )
            }
            // Check if message contains a notification payload.
            if (remoteMessage.notification != null) {
                Log.d(
                    TAG,
                    "Message Notification Body: " + remoteMessage.notification!!.body
                )
            }
            if (remoteMessage.data.isNotEmpty()) {
                val extras = Bundle()
                for ((key, value) in remoteMessage.data) {
                    extras.putString(key, value)
                }
                val info = CleverTapAPI.getNotificationInfo(extras)
                var message: String?
                val imageUri: String?
                val link: String?
                val which: String?
                val title: String?
                if (info.fromCleverTap) {
                    if (extras.getString("nm") != "" || extras.getString("nm") != null
                    ) {
                        message = extras.getString("message")
                        message = extras.getString("nm")
                        imageUri = extras.getString("image")
                        link = extras.getString("link")
                        which = extras.getString("which")
                        title = extras.getString("title")
                        bitmap = getBitmapfromUrl(imageUri)
                        if (message != null) {
                            if (message != "") {
                                sendNotification(message, bitmap, which, link, title)
                            } else {
                                CleverTapAPI.getDefaultInstance(
                                    applicationContext
                                )?.pushNotificationViewedEvent(extras)
                            }
                        }
                    }
                } else {
                    message = remoteMessage.data["message"]
                    imageUri = remoteMessage.data["image"]
                    link = remoteMessage.data["link"]
                    which = remoteMessage.data["which"]
                    title = remoteMessage.data["title"]
                    bitmap = getBitmapfromUrl(imageUri)
                    sendNotification(message, bitmap, which, link, title)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private fun sendNotification(
        messageBody: String?,
        image: Bitmap?,
        which: String?,
        link: String?,
        title: String?
    ) {
        try {
            Log.i("Result", "Got the data yessss")
            val rand = Random()
            val a = rand.nextInt(101) + 1
            val intent =
                Intent(applicationContext, FCM_TARGET_ACTIVITY)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("which", which)
            intent.putExtra("link", link)
            intent.putExtra("title", title)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
            )
            val defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(applicationContext)
                    .setLargeIcon(image) /*Notification icon image*/
                    .setSmallIcon(FCM_ICON)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(image)
                    ) /*Notification with Image*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_DEFAULT)
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // The id of the channel.
                val id = "messenger_general"
                val name: CharSequence = "General"
                val description = "General Notifications sent by the app"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(id, name, importance)
                mChannel.description = description
                mChannel.enableLights(true)
                mChannel.lightColor = Color.BLUE
                mChannel.enableVibration(true)
                notificationManager.createNotificationChannel(mChannel)
                notificationManager.notify(a + 1, notificationBuilder.setChannelId(id).build())
            } else {
                notificationManager.notify(
                    a + 1 /* ID of notification */,
                    notificationBuilder.build()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "FirebaseMessageService"
        fun checkForNotifications(
            context: Context,
            intent: Intent,
            webViewActivityToOpen: Class<out Activity?>?,
            activityToOpen: Class<out Activity?>?,
            intentParam: String
        ) {
            try {
                val which = intent.getStringExtra("which")
                val url = intent.getStringExtra("link")
                val title = intent.getStringExtra("title")

                when (which) {
                    "B" -> {
                        try {
                            val intent1 = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent1)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "Unable to open the link", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    "P" -> {
                        try {
                            val intent1 = Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    "market://details?id=$url"
                                )
                            )
                            context.startActivity(intent1)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                            val intent1 = Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    "https://play.google.com/store/apps/details?id=$url"
                                )
                            )
                            context.startActivity(intent1)
                        }
                    }
                    "L" -> {
                        try {
                            val intent1 = Intent(context, webViewActivityToOpen)
                            intent1.putExtra("link", url)
                            intent1.putExtra("title", title)
                            context.startActivity(intent1)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    "D" -> {
                        try {
                            val intent1 = Intent(context, activityToOpen)
                            intent1.putExtra(intentParam, url)
                            context.startActivity(intent1)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    else -> {
                        Log.d(TAG, "No event fired")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "checkForNotifications: \$e")
//                Dont push
            }
        }
    }
}