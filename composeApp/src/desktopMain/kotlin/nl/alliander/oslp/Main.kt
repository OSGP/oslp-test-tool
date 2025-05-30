package nl.alliander.oslp

import JvmSocketService
import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import nl.alliander.oslp.service.LoggingService

fun main() = application {
    val loggingService = remember { LoggingService() }
    val socketService = remember { JvmSocketService() }

    LaunchedEffect(Unit) {
        socketService.log = { loggingService.appendLog(it) }
        socketService.onMessage = { msg -> loggingService.appendLog("Bericht: $msg") }
        socketService.startListening()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "oslp-test-tool",
    ) {
        App(loggingService)
    }
}
