package nl.alliander.oslp

import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import nl.alliander.oslp.models.MainViewModel
import nl.alliander.oslp.service.DeviceStateService
import nl.alliander.oslp.service.LoggingService
import nl.alliander.oslp.service.RequestService
import nl.alliander.oslp.sockets.ServerSocket

fun main() = application {
    val loggingService = remember { LoggingService() }
    val requestService = remember { RequestService(loggingService) }

    val mainViewModel = remember { MainViewModel() }

    DeviceStateService.createInstance(mainViewModel)

    val serverSocket = remember {
        ServerSocket(loggingService)
    }

    serverSocket.startListening()

    val state = rememberWindowState(
        width = 1280.dp,
        height = 720.dp,
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "OSLP Test Tool",
        state = state,
    ) {
        App(loggingService, requestService, mainViewModel)
    }
}
