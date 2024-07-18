import alert.AppContext
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.NotificationCompat

actual fun Notify(message: String, duration: NotificationDuration) {
    val context = AppContext.get()
    val length = if (duration == NotificationDuration.LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(context, message, length).show()
}
actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.TOAST -> object : Notification() {
        override fun show(message: String) {
            val context = AppContext.get()
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    NotificationType.TOP -> object : Notification() {
        override fun show(message: String) {
            val context = AppContext.get()
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
                .setContentTitle("Top Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notification)
        }
    }
    NotificationType.CUSTOM -> object : Notification() {
        override fun show(message: String) {
            val context = AppContext.get()
            val dialog = AlertDialog.Builder(context)
                .setTitle("Custom Notification")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
            dialog.show()
        }
    }
    else -> throw IllegalArgumentException("Unsupported notification type")
}