// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.lfenergy.gxf.oslp.models.ApplicationConfiguration
import org.lfenergy.gxf.oslp.models.MainViewModel
import org.lfenergy.gxf.oslp.service.DeviceStateService
import org.lfenergy.gxf.oslp.service.RequestService
import org.lfenergy.gxf.oslp.sockets.ServerSocket

fun main() = application {
    val requestService = remember { RequestService() }

    val mainViewModel = remember { MainViewModel() }

    DeviceStateService.createInstance(mainViewModel)

    val serverSocket = remember { ServerSocket() }

    val state = rememberWindowState(width = 1280.dp, height = 720.dp)

    val configurationViewModel = ApplicationConfiguration.get().toModel()

    Window(onCloseRequest = ::exitApplication, title = "OSLP Test Tool", state = state) {
        var isConfigured by remember { mutableStateOf(false) }

        if (!isConfigured) {
            ConfigurationScreen(
                configurationViewModel,
                onContinue = {
                    val config = ApplicationConfiguration.update(configurationViewModel)

                    config.storeConfiguration()
                },
            )
        } else {
            LaunchedEffect(Unit) { serverSocket.startListening() }
            App(requestService, mainViewModel)
        }
    }
}
