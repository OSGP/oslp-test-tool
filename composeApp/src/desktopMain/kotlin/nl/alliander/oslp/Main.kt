package nl.alliander.oslp

import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import nl.alliander.oslp.models.MainViewModel
import nl.alliander.oslp.service.DeviceStateService
import nl.alliander.oslp.service.RequestService
import nl.alliander.oslp.sockets.ServerSocket

fun main() = application {
    val requestService = remember { RequestService() }

    val mainViewModel = remember { MainViewModel() }

    DeviceStateService.createInstance(mainViewModel)

    val serverSocket = remember {
        ServerSocket()
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
        App(requestService, mainViewModel)
    }
}
