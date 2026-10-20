package io.github.khubaibkhan4.alert.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Alert-KMP demo") { App() }
}
