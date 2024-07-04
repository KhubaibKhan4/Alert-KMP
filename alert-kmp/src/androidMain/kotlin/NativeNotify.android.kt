import alert.AppContext
import android.widget.Toast

actual fun Notify(message: String) {
    val context = AppContext.get()
    Toast.makeText(
        context, message, Toast.LENGTH_SHORT
    ).show()
}