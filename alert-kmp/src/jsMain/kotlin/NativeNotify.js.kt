import kotlinx.browser.window
import org.w3c.notifications.DEFAULT
import org.w3c.notifications.DENIED
import org.w3c.notifications.GRANTED
import org.w3c.notifications.Notification
import org.w3c.notifications.NotificationOptions
import org.w3c.notifications.NotificationPermission

 actual fun Notify(message: String) {
    window.alert(message)
    if (!js("typeof Notification !== 'undefined'").unsafeCast<Boolean>()) {
        window.alert(message)
        return
    }
    when (Notification.permission) {
        NotificationPermission.GRANTED -> {
            showNotification(message)
        }
        NotificationPermission.DENIED -> {
            window.alert(message)
        }
        NotificationPermission.DEFAULT -> {
            Notification.requestPermission().then { permission ->
                if (permission == NotificationPermission.GRANTED) {
                    showNotification(message)
                } else {
                    window.alert(message)
                }
            }
        }
    }
}
private fun showNotification(message: String) {
    val options = NotificationOptions(
        body = message
    )
    Notification("New Notification", options)
}