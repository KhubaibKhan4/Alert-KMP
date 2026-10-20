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
    toast(message, duration)
}

actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.TOAST -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            toast(message, duration)
        }
    }
    NotificationType.ALERT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            dialog(message, duration) { activity ->
                AlertDialog.Builder(activity)
                    .setTitle(title ?: "Alert Notification")
                    .setMessage(message)
                    .setPositiveButton("OK", null)
            }
        }
    }
    NotificationType.TOP -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val context = AppContext.get()
            if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                Log.e("Notification", "Notifications are disabled. Opening the app's notification settings...")
                AppContext.onMain { openNotificationSettings(context) }
                return
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
    is NotificationType.ICON_ALERT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            dialog(message, duration) { activity ->
                val icon = activity.resources.getIdentifier(type.iconName, "drawable", activity.packageName)
                AlertDialog.Builder(activity)
                    .setTitle(title ?: "Notification")
                    .setMessage(message)
                    .apply { if (icon != 0) setIcon(icon) }
                    .setPositiveButton("OK", null)
            }
        }
    }
    is NotificationType.CUSTOM -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            dialog(message, duration) { activity ->
                AlertDialog.Builder(activity)
                    .setTitle(type.title)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
            }
        }
    }
    is NotificationType.MENU_CONTEXT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            dialog(message, duration) { activity ->
                AlertDialog.Builder(activity)
                    .setTitle(title ?: "Context Menu Notification")
                    .setItems(type.actions.map { it.first }.toTypedArray()) { _, which ->
                        type.actions[which].second.invoke()
                    }
            }
        }
    }
}

private fun toast(message: String, duration: NotificationDuration) {
    val toastDuration = if (duration == NotificationDuration.LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    AppContext.onMain { Toast.makeText(AppContext.get(), message, toastDuration).show() }
}

/** Shows a dialog on the Activity in front; without one (the app is in the background) the message is a toast. */
private fun dialog(message: String, duration: NotificationDuration, build: (Activity) -> AlertDialog.Builder) {
    AppContext.onMain {
        val activity = AppContext.activity()
        if (activity == null) {
            Log.w("Notification", "No Activity in front to show a dialog; showing a toast instead.")
            toast(message, duration)
        } else {
            build(activity).create().show()
        }
    }
}

private fun openNotificationSettings(context: Context) {
    val intent = Intent().apply {
        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        // Started from the Application context, outside any Activity's task.
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
