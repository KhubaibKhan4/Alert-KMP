import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.notifications.DEFAULT
import org.w3c.notifications.DENIED
import org.w3c.notifications.GRANTED
import org.w3c.notifications.NotificationOptions
import org.w3c.notifications.NotificationPermission
import org.w3c.notifications.Notification as NotificationManager

actual fun Notify(message: String, duration: NotificationDuration) {
    window.alert(message)
    if (!js("typeof Notification !== 'undefined'").unsafeCast<Boolean>()) {
        window.alert(message)
        return
    }
    when (NotificationManager.permission) {
        NotificationPermission.GRANTED -> {
            showNotification(message, duration)
        }
        NotificationPermission.DENIED -> {
            window.alert(message)
        }
        NotificationPermission.DEFAULT -> {
            NotificationManager.requestPermission().then { permission ->
                if (permission == NotificationPermission.GRANTED) {
                    showNotification(message, duration)
                } else {
                    window.alert(message)
                }
            }
        }
    }
}

private fun showNotification(message: String, duration: NotificationDuration) {
    val options = NotificationOptions(
        body = message,
        timestamp = if (duration == NotificationDuration.LONG) 3000 else 1500
    )
    NotificationManager("New Notification", options)
}

actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.TOAST -> object : Notification() {
        override fun show(message: String) {
            val toast = document.createElement("div")
            toast.textContent = message
            toast.setAttribute("style", """
                position: fixed;
                bottom: 10px;
                left: 50%;
                transform: translateX(-50%);
                background: rgba(0, 0, 0, 0.7);
                color: #fff;
                padding: 10px 20px;
                border-radius: 5px;
                opacity: 0;
                transition: opacity 0.5s ease-in-out;
                z-index: 1000;
            """.trimIndent())
            document.body?.appendChild(toast)
            window.setTimeout({
                toast.setAttribute("style", toast.getAttribute("style") + "opacity: 1;")
            }, 100)
            window.setTimeout({
                toast.setAttribute("style", toast.getAttribute("style") + "opacity: 0;")
                window.setTimeout({
                    document.body?.removeChild(toast)
                }, 500)
            }, 3000)
        }
    }
    NotificationType.ALERT -> object : Notification() {
        override fun show(message: String) {
            window.alert(message)
        }
    }
    NotificationType.TOP -> object : Notification() {
        override fun show(message: String) {
            val notification = document.createElement("div")
            val closeButton = document.createElement("span")

            notification.textContent = message
            notification.setAttribute("style", """
                position: fixed;
                top: 0;
                width: 100%;
                background: #333;
                color: #fff;
                padding: 10px;
                text-align: center;
                z-index: 1000;
            """.trimIndent())

            closeButton.textContent = "✖"
            closeButton.setAttribute("style", """
                margin-left: 10px;
                cursor: pointer;
                float: right;
            """.trimIndent())

            closeButton.addEventListener("click", {
                document.body?.removeChild(notification)
            })

            notification.appendChild(closeButton)
            document.body?.appendChild(notification)
        }
    }
    NotificationType.CUSTOM -> object : Notification() {
        override fun show(message: String) {
            val notification = document.createElement("div")
            val closeButton = document.createElement("span")

            notification.textContent = message
            notification.setAttribute("style", """
                position: fixed;
                bottom: 0;
                right: 0;
                background: #007BFF;
                color: #fff;
                padding: 20px;
                border-radius: 5px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                z-index: 1000;
            """.trimIndent())

            closeButton.textContent = "✖"
            closeButton.setAttribute("style", """
                margin-left: 10px;
                cursor: pointer;
                float: right;
            """.trimIndent())

            closeButton.addEventListener("click", {
                document.body?.removeChild(notification)
            })

            notification.appendChild(closeButton)
            document.body?.appendChild(notification)
        }
    }
    else -> throw IllegalArgumentException("Unsupported notification type")
}