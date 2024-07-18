import platform.UIKit.*
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIView
import platform.UserNotifications.*
import platform.darwin.*

actual fun Notify(message: String, duration: NotificationDuration) {
    val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController?.modalViewController
    val alertController = UIAlertController.alertControllerWithTitle(
        title = UIDevice.currentDevice.systemName,
        message = message,
        preferredStyle = UIAlertControllerStyle.MAX_VALUE
    )
    alertController.addAction(
        UIAlertAction.actionWithTitle(
            "OK",
            style = UIAlertControllerStyle.MAX_VALUE,
            handler = null
        )
    )
    viewController?.presentViewController(alertController, animated = true, completion = {
        val delay = if (duration == NotificationDuration.LONG) 3.0 else 1.5
        NSTimer.scheduledTimerWithTimeInterval(delay, repeats = false) {
            alertController.dismissViewControllerAnimated(true, completion = null)
        }
    })
}
actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.ALERT -> object : Notification() {
        override fun show(message: String) {
            val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            val alertController = UIAlertController.alertControllerWithTitle(
                title = UIDevice.currentDevice.systemName,
                message = message,
                preferredStyle = UIAlertControllerStyle.Alert
            )
            alertController.addAction(
                UIAlertAction.actionWithTitle("OK", UIAlertActionStyle.Default, null)
            )
            viewController?.presentViewController(alertController, animated = true, completion = null)
        }
    }
    NotificationType.TOP -> object : Notification() {
        override fun show(message: String) {
            val center = UNUserNotificationCenter.currentNotificationCenter()

            center.requestAuthorizationWithOptions(
                options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound
            ) { granted, error ->
                if (granted) {
                    val content = UNMutableNotificationContent().apply {
                        title = "Top Notification"
                        body = message
                        sound = UNNotificationSound.defaultSound()
                    }

                    val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(1.0, false)

                    val request = UNNotificationRequest.requestWithIdentifier(
                        "topNotification",
                        content,
                        trigger
                    )

                    center.addNotificationRequest(request) { error ->
                        error?.let {
                            NSLog("Error scheduling notification: ${it.localizedDescription}")
                        }
                    }
                } else {
                    NSLog("Permission not granted for notifications: ${error?.localizedDescription ?: "No error"}")
                }
            }
        }
    }
    NotificationType.CUSTOM -> object : Notification() {
        override fun show(message: String) {
            val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            val customAlertController = UIAlertController.alertControllerWithTitle(
                title = "Custom Notification",
                message = message,
                preferredStyle = UIAlertControllerStyle.ActionSheet
            )
            customAlertController.addAction(
                UIAlertAction.actionWithTitle("OK", UIAlertActionStyle.Default, null)
            )
            viewController?.presentViewController(customAlertController, animated = true, completion = null)
        }
    }
    else -> throw IllegalArgumentException("Unsupported notification type")
}