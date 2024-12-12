import alert.AppContext
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

actual fun Notify(message: String, duration: NotificationDuration) {
    val context = AppContext.get()
    val toastDuration = if (duration == NotificationDuration.LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(context, message, toastDuration).show()
}

actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.TOAST -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val context = AppContext.get()
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    NotificationType.ALERT -> object : Notification() {

        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val context = AppContext.get()
            AlertDialog.Builder(context)
                .setTitle("Alert Notification")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
                .show()
            Log.d("Notification", "Alert notification shown with message: $message")
        }
    }
    NotificationType.TOP -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val context = AppContext.get()
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                NotificationManagerCompat.from(context).areNotificationsEnabled().also { areEnabled ->
                    if (!areEnabled) {
                        Log.e("Notification", "Notifications are disabled. Requesting permission...")
                        openNotificationSettings(context)
                        return
                    }
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "top_notification_channel",
                    "Top Notification",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Channel for top notifications"
                }
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, "top_notification_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title ?: "Top Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notification)
            Log.d("Notification", "Top notification shown with message: $message")
        }
    }
    is NotificationType.CUSTOM -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val activity = AppContext.get() as? Activity
            if (activity != null) {
                val dialog = AlertDialog.Builder(activity)
                    .setTitle(type.title)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .create()
                dialog.show()
                Log.d("Notification", "Custom notification shown with message: $message")
            } else {
                Log.e("CustomNotification", "Context is not an Activity")
            }
        }
    }
    is NotificationType.MENU_CONTEXT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val activity = AppContext.get() as? Activity
            if (activity != null) {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle(title ?: "Context Menu Notification")
                builder.setItems(type.actions.map { it.first }.toTypedArray()) { _, which ->
                    type.actions[which].second.invoke()
                }
                builder.create().show()
                Log.d("Notification", "MenuContext notification shown with options")
            } else {
                Log.e("MenuContextNotification", "Context is not an Activity")
            }
        }
    }
    else -> throw IllegalArgumentException("Unsupported notification type")
}

private fun openNotificationSettings(context: Context) {
    val intent = Intent().apply {
        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}