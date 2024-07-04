import kotlinx.browser.window

actual fun Notify(message: String) {
    window.alert(message)
}