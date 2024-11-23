import kotlinx.serialization.Serializable

expect fun Notify(message: String, duration: NotificationDuration = NotificationDuration.SHORT)
enum class NotificationDuration {
    SHORT, LONG
}
@Serializable
sealed class NotificationType {
    object ALERT : NotificationType()
    object TOAST : NotificationType()
    object TOP : NotificationType()
    data class ICON_ALERT(val iconName: String) : NotificationType()
    data class MENU_CONTEXT(val actions: List<Pair<String, () -> Unit>>) : NotificationType()
    data class CUSTOM(val title: String, val iconName: String? = null) : NotificationType()
}

abstract class Notification {
    abstract fun show(message: String, title: String? = null, duration: NotificationDuration = NotificationDuration.SHORT)
}
expect fun createNotification(type: NotificationType): Notification