package nl.alliander.oslp

import JvmSocketService
import SocketService
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {

    val socketService: SocketService = JvmSocketService()
    
    socketService.startListening()

    Window(
        onCloseRequest = ::exitApplication,
        title = "oslp-test-tool",
    ) {
        App()
    }
}
