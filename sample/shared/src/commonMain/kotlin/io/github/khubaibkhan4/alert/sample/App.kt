package io.github.khubaibkhan4.alert.sample

import NotificationDuration
import NotificationType
import Notify
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import createNotification

/** Every Alert-KMP notification type, one button each. */
@Composable
fun App() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            var title by remember { mutableStateOf("Alert-KMP") }
            var message by remember { mutableStateOf("Hello from Kotlin Multiplatform!") }
            var lastChoice by remember { mutableStateOf("") }

            Column(
                Modifier.safeDrawingPadding().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Alert-KMP demo", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(title, { title = it }, Modifier.fillMaxWidth(), label = { Text("Title") })
                OutlinedTextField(message, { message = it }, Modifier.fillMaxWidth(), label = { Text("Message") })

                fun show(type: NotificationType) = createNotification(type).show(message, title)
                DemoButton("Toast") { show(NotificationType.TOAST) }
                DemoButton("Alert") { show(NotificationType.ALERT) }
                DemoButton("Top (system notification)") { show(NotificationType.TOP) }
                DemoButton("Alert with icon") { show(NotificationType.ICON_ALERT("ic_dialog_info")) }
                DemoButton("Custom") { show(NotificationType.CUSTOM(title = "Custom: $title")) }
                DemoButton("Menu") {
                    show(NotificationType.MENU_CONTEXT(listOf(
                        "Reply" to { lastChoice = "Reply" },
                        "Archive" to { lastChoice = "Archive" },
                        "Delete" to { lastChoice = "Delete" },
                    )))
                }
                OutlinedButton({ Notify(message, NotificationDuration.LONG) }, Modifier.fillMaxWidth()) { Text("Notify(message)") }
                if (lastChoice.isNotEmpty()) Text("Menu choice: $lastChoice")
            }
        }
    }
}

@Composable
private fun DemoButton(text: String, onClick: () -> Unit) {
    Button(onClick, Modifier.fillMaxWidth()) { Text(text) }
}
