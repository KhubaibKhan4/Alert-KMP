import platform.Foundation.NSLog
import platform.Foundation.NSTimer
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyle
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual fun Notify(message: String, duration: NotificationDuration) {
    val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    if (viewController != null) {
        val alertController = UIAlertController.alertControllerWithTitle(
            title = UIDevice.currentDevice.systemName,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )
        alertController.addAction(
            UIAlertAction.actionWithTitle(
                "OK",
                style = UIAlertActionStyleDefault,
                handler = null
            )
        )

        dispatch_async(dispatch_get_main_queue()) {
            viewController.presentViewController(alertController, animated = true, completion = {
                val delay = if (duration == NotificationDuration.LONG) 3.0 else 1.5
                NSTimer.scheduledTimerWithTimeInterval(delay, repeats = false) {
                    alertController.dismissViewControllerAnimated(true, completion = null)
                }
            })
        }
    } else {
        NSLog("Error: rootViewController is null")
    }
}

actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.ALERT -> object : Notification() {
        override fun show(message: String) {
            val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            val alertController = UIAlertController.alertControllerWithTitle(
                title = UIDevice.currentDevice.systemName,
                message = message,
                preferredStyle = UIAlertControllerStyleAlert
            )
            alertController.addAction(
                UIAlertAction.actionWithTitle("OK", UIAlertActionStyleDefault, null)
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
                        setTitle("Top Notification")
                        setBody(message)
                        setSound(UNNotificationSound.defaultSound())
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
                preferredStyle = UIAlertControllerStyleActionSheet
            )
            customAlertController.addAction(
                UIAlertAction.actionWithTitle("OK", UIAlertActionStyleDefault, null)
            )
            viewController?.presentViewController(customAlertController, animated = true, completion = null)
        }
    }
    else -> throw IllegalArgumentException("Unsupported notification type")
}