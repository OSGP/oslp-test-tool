package nl.alliander.oslp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "oslp-test-tool",
    ) {
        App()
    }
}