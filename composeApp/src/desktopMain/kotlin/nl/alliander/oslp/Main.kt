package nl.alliander.oslp

import JvmSocketService
import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import nl.alliander.oslp.service.LoggingService

fun main() = application {

    val loggingService = remember { LoggingService() }
    val socketService = remember { JvmSocketService() }

    LaunchedEffect(Unit) {
        socketService.onMessage = { msg ->
            loggingService.appendLog(msg);
        }
        socketService.startListening()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "oslp-test-tool",
    ) {
        App(loggingService)
    }
}
