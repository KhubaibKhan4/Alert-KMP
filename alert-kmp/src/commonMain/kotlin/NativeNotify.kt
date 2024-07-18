expect fun Notify(message: String, duration: NotificationDuration = NotificationDuration.SHORT)
enum class NotificationDuration {
    SHORT, LONG
}
enum class NotificationType {
    TOAST, ALERT, TOP, CUSTOM
}
abstract class Notification {
    abstract fun show(message: String)
}
expect fun createNotification(type: NotificationType): Notification