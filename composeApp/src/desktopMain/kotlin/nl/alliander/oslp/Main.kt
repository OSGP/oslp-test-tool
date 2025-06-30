// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package nl.alliander.oslp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.io.File
import kotlinx.serialization.json.Json
import nl.alliander.oslp.models.ApplicationConfiguration
import nl.alliander.oslp.models.ApplicationConfigurationViewModel
import nl.alliander.oslp.models.MainViewModel
import nl.alliander.oslp.service.DeviceStateService
import nl.alliander.oslp.service.RequestService
import nl.alliander.oslp.sockets.ServerSocket
import nl.alliander.oslp.util.SigningUtil

fun main() = application {
    val requestService = remember { RequestService() }

    val mainViewModel = remember { MainViewModel() }

    DeviceStateService.createInstance(mainViewModel)

    val serverSocket = remember { ServerSocket() }

    val state = rememberWindowState(width = 1280.dp, height = 720.dp)

    Window(onCloseRequest = ::exitApplication, title = "OSLP Test Tool", state = state) {
        var isConfigured by remember { mutableStateOf(false) }

        if (!isConfigured) {
            ConfigurationScreen(onContinue = {
                isConfigured = SigningUtil.initializeKeys()
                storeConfiguration()
            })
        } else {
            LaunchedEffect(Unit) { serverSocket.startListening() }
            App(requestService, mainViewModel)
        }
    }
}

fun storeConfiguration() {
    val config = ApplicationConfigurationViewModel.getInstance()
    val file = File("app_config.json")
    file.writeText(Json.encodeToString(ApplicationConfiguration.fromModel(config)))

    println("Configuratie opgeslagen naar ${file.absolutePath}")
}
