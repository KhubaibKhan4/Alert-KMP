import java.awt.BorderLayout
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.util.Timer
import java.util.TimerTask
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingConstants


actual fun Notify(message: String, duration: NotificationDuration) {
    if (SystemTray.isSupported()) {
        val tray = SystemTray.getSystemTray()
        val image = Toolkit.getDefaultToolkit().createImage("logo.webp")
        val trayIcon = TrayIcon(image, "Desktop Notification")
        tray.add(trayIcon)
        trayIcon.displayMessage("Desktop Notification", message, TrayIcon.MessageType.INFO)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                tray.remove(trayIcon)
            }
        }, if (duration == NotificationDuration.LONG) 3000 else 1500)
    } else {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Desktop Notification",
            JOptionPane.INFORMATION_MESSAGE
        )
    }
}

actual fun createNotification(type: NotificationType): Notification = when (type) {
    NotificationType.TOAST -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            JOptionPane.showMessageDialog(
                null,
                message,
                title ?: "Notification",
                JOptionPane.INFORMATION_MESSAGE
            )
        }
    }
    NotificationType.TOP -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val frame = JFrame()
            frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            val label = JLabel(message)
            label.horizontalAlignment = SwingConstants.CENTER
            frame.add(label, BorderLayout.NORTH)
            frame.setSize(300, 100)
            frame.isVisible = true
        }
    }
     is NotificationType.CUSTOM -> object : Notification() {
         override fun show(message: String, title: String?, duration: NotificationDuration) {
             val frame = JFrame(title)
             frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
             val panel = JPanel()
             panel.add(JLabel(message))
             frame.contentPane.add(panel)
             frame.setSize(300, 100)
             frame.isVisible = true
         }
     }
    NotificationType.ALERT, is NotificationType.ICON_ALERT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            JOptionPane.showMessageDialog(null, message, title ?: "Alert", JOptionPane.INFORMATION_MESSAGE)
        }
    }
    is NotificationType.MENU_CONTEXT -> object : Notification() {
        override fun show(message: String, title: String?, duration: NotificationDuration) {
            val labels = type.actions.map { it.first }.toTypedArray<Any>()
            val choice = JOptionPane.showOptionDialog(
                null, message, title ?: "Choose an Action", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, labels, null
            )
            if (choice >= 0) type.actions[choice].second.invoke()
        }
    }
}
