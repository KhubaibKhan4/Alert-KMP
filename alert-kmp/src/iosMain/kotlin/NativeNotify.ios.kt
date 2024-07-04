import platform.UIKit.*
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIView
import platform.darwin.*

actual fun Notify(message: String) {
    val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController?.modalViewController
    val alertController = UIAlertController.alertControllerWithTitle(
        title = UIDevice.currentDevice.systemName,
        message = message,
        preferredStyle = UIAlertControllerStyle.MAX_VALUE
    )
    alertController.addAction(
        UIAlertAction.actionWithTitle(
            "OK",
            style = UIAlertControllerStyle.MAX_VALUE,
            handler = null
        )
    )
    viewController?.presentViewController(alertController, animated = true, completion = null)
}