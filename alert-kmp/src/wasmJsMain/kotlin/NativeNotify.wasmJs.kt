import kotlinx.browser.document
import kotlinx.browser.window

actual fun Notify(message: String, duration: NotificationDuration) {
    window.alert(message)
}
var toastCounter = 0
actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.TOAST -> object : Notification() {
        override fun show(message: String) {
            toastCounter++
            val toastId = "toast_$toastCounter"
            val toast = document.createElement("div")
            toast.id = toastId
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
            showToast(toastId, 3000)
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
