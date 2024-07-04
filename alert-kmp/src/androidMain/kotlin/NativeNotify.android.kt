import alert.AppContext
import android.widget.Toast

internal actual fun Notify(message: String) {
    val context = AppContext.get()
    Toast.makeText(
        context, message, Toast.LENGTH_SHORT
    ).show()
}