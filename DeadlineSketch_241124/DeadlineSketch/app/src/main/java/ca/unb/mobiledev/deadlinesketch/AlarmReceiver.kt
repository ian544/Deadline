package ca.unb.mobiledev.deadlinesketch

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import ca.unb.mobiledev.deadlinesketch.Constants.NOTIFICATION_CHANNEL_ID

class AlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "it worked")
        val notifID = intent.getIntExtra("notifID", 0)
        val title = intent.getStringExtra("title") ?: "Notification"
        val message = intent.getStringExtra("message") ?: "Task requires attention."
        val priority = intent.getStringExtra("priority") ?: "None"
        handleNotification(context, notifID, title, message, priority)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleNotification(context: Context, notifID: Int, title: String, message: String, priority: String) {
        val channelID = "channel_01"
        val channelName = "Default Channel"
        val channelDesc = "Default notification channel"
        lateinit var builder: NotificationCompat.Builder
        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = channelDesc
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        val notifIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 2, notifIntent, PendingIntent.FLAG_IMMUTABLE)
        builder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_round)
            .setContentTitle(title)
            .setContentText("Task Priority: $priority\n$message")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(notifID, builder.build())
        }
    }
}