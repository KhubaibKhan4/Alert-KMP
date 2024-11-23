import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSLog
import platform.Foundation.NSTimer
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIDevice
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UILabel
import platform.UIKit.UIView
import platform.UIKit.UIViewAnimationOptionCurveEaseInOut
import platform.UIKit.UIViewContentMode
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

@OptIn(ExperimentalForeignApi::class)
actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.ALERT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            val alertController = UIAlertController.alertControllerWithTitle(
                title = title ?: UIDevice.currentDevice.systemName,
                message = message,
                preferredStyle = UIAlertControllerStyleAlert
            )
            alertController.addAction(
                UIAlertAction.actionWithTitle("OK", UIAlertActionStyleDefault, null)
            )
            dispatch_async(dispatch_get_main_queue()) {
                viewController?.presentViewController(alertController, animated = true, completion = null)
            }
        }
    }

    NotificationType.TOAST -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            if (viewController != null) {
                dispatch_async(dispatch_get_main_queue()) {
                    val toastView = UIView(frame = CGRectMake(0.0, 0.0, 300.0, 50.0)).apply {
                        backgroundColor = UIColor.blackColor()
                        alpha = 0.7
                        layer.cornerRadius = 10.0
                        center = viewController.view.center
                    }

                    val messageLabel = UILabel(frame = toastView.bounds).apply {
                        text = message
                        textColor = UIColor.whiteColor()
                        textAlignment = NSTextAlignmentCenter
                        numberOfLines = 2
                    }

                    toastView.addSubview(messageLabel)
                    viewController.view.addSubview(toastView)

                    UIView.animateWithDuration(
                        0.5,
                        animations = { toastView.alpha = 1.0 },
                        completion = {
                            UIView.animateWithDuration(
                                0.5,
                                delay = if (duration == NotificationDuration.LONG) 2.5 else 1.5,
                                options = UIViewAnimationOptionCurveEaseInOut,
                                animations = { toastView.alpha = 0.0 },
                                completion = { toastView.removeFromSuperview() }
                            )
                        }
                    )
                }
            }
        }
    }

    is NotificationType.ICON_ALERT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            val icon = UIImage.imageNamed(type.iconName)

            val alertController = UIAlertController.alertControllerWithTitle(
                title = title ?: "Notification",
                message = message,
                preferredStyle = UIAlertControllerStyleAlert
            )

            val iconView = UIImageView(frame = CGRectMake(0.0, 0.0, 40.0, 40.0)).apply {
                setImage(icon)
                contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
            }

            alertController.view.addSubview(iconView)

            alertController.addAction(
                UIAlertAction.actionWithTitle("OK", UIAlertActionStyleDefault, null)
            )

            dispatch_async(dispatch_get_main_queue()) {
                viewController?.presentViewController(alertController, animated = true, completion = null)
            }
        }
    }

    is NotificationType.MENU_CONTEXT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController

            val menuController = UIAlertController.alertControllerWithTitle(
                title = title ?: "Choose an Action",
                message = message,
                preferredStyle = UIAlertControllerStyleActionSheet
            )

            type.actions.forEach { (actionTitle, actionCallback) ->
                menuController.addAction(
                    UIAlertAction.actionWithTitle(actionTitle, UIAlertActionStyleDefault) {
                        actionCallback()
                    }
                )
            }

            menuController.addAction(
                UIAlertAction.actionWithTitle("Cancel", UIAlertActionStyleCancel, null)
            )

            dispatch_async(dispatch_get_main_queue()) {
                viewController?.presentViewController(menuController, animated = true, completion = null)
            }
        }
    }

    else -> throw IllegalArgumentException("Unsupported notification type")
}