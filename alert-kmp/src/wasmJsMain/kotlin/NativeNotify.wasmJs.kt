import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element

actual fun Notify(message: String, duration: NotificationDuration) {
    window.alert(message)
}

actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.TOAST -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val toast = document.createElement("div")
            toast.textContent = message
            val style = """
                position: fixed;
                bottom: 10px;
                left: 50%;
                transform: translateX(-50%);
                background: rgba(0, 0, 0, 0.7);
                color: #fff;
                padding: 10px 20px;
                border-radius: 5px;
                transition: opacity 0.5s ease-in-out;
                z-index: 1000;
            """.trimIndent()
            toast.setAttribute("style", "$style opacity: 0;")
            document.body?.appendChild(toast)
            window.setTimeout({ toast.setAttribute("style", "$style opacity: 1;"); null }, 100)
            window.setTimeout({
                toast.setAttribute("style", "$style opacity: 0;")
                window.setTimeout({ toast.remove(); null }, 500)
                null
            }, if (duration == NotificationDuration.LONG) 3500 else 2000)
        }
    }
    NotificationType.ALERT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            window.alert(if (title != null) "$title\n\n$message" else message)
        }
    }
    is NotificationType.ICON_ALERT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            window.alert(if (title != null) "$title\n\n$message" else message)
        }
    }
    NotificationType.TOP -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            closable(message, """
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                box-sizing: border-box;
                background: #333;
                color: #fff;
                padding: 10px;
                text-align: center;
                z-index: 1000;
            """.trimIndent())
        }
    }
    is NotificationType.CUSTOM -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            closable("${type.title}: $message", """
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
        }
    }
    is NotificationType.MENU_CONTEXT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val menu = closable(title ?: message, """
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background: #fff;
                color: #222;
                padding: 16px;
                border-radius: 8px;
                box-shadow: 0 4px 24px rgba(0, 0, 0, 0.25);
                z-index: 1000;
            """.trimIndent())
            type.actions.forEach { (label, action) ->
                val button = document.createElement("button")
                button.textContent = label
                button.setAttribute("style", "display: block; width: 100%; margin-top: 8px; padding: 8px; cursor: pointer;")
                button.addEventListener("click", { menu.remove(); action() })
                menu.appendChild(button)
            }
        }
    }
}

/** A box with [message] and a ✖ that removes it. */
private fun closable(message: String, style: String): Element {
    val box = document.createElement("div")
    val text = document.createElement("span")
    val closeButton = document.createElement("span")
    text.textContent = message
    box.setAttribute("style", style)
    closeButton.textContent = "✖"
    closeButton.setAttribute("style", "margin-left: 10px; cursor: pointer; float: right;")
    closeButton.addEventListener("click", { box.remove() })
    box.appendChild(closeButton)
    box.appendChild(text)
    document.body?.appendChild(box)
    return box
}
