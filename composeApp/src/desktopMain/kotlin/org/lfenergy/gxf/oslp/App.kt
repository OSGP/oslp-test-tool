// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import javax.swing.JFileChooser
import org.lfenergy.gxf.oslp.components.CircleIndicator
import org.lfenergy.gxf.oslp.components.CommandButton
import org.lfenergy.gxf.oslp.components.SetLightRow
import org.lfenergy.gxf.oslp.models.MainViewModel
import org.lfenergy.gxf.oslp.service.RequestService
import org.lfenergy.gxf.oslp.util.Logger

@Composable
@androidx.compose.desktop.ui.tooling.preview.Preview
fun App(requestService: RequestService, viewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Logger.loggingText) { scrollState.animateScrollTo(scrollState.maxValue) }

    MaterialTheme {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val modifier = Modifier.weight(1f).height(60.dp)
            Column(modifier = Modifier.width(300.dp).padding(end = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircleIndicator(viewModel.isDeviceRegistered, "Register device")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircleIndicator(viewModel.isConfirmed, "Confirm registration")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CommandButton("Selftest (10 sec)", modifier, viewModel.isConfirmed) {
                        requestService.startSelfTest()
                    }
                    CommandButton("Reboot", modifier, viewModel.isConfirmed && viewModel.isCommunicationEnabled) {
                        requestService.startReboot()
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CommandButton("Light sensor on", modifier, viewModel.isConfirmed) {
                        requestService.setLightSensor(0)
                    }
                    CommandButton("Light sensor off", modifier, viewModel.isConfirmed) {
                        requestService.setLightSensor(1)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CommandButton("Get Status", modifier, viewModel.isConfirmed) { requestService.getStatus() }
                    CommandButton("Get Configuration", modifier, viewModel.isConfirmed) {
                        requestService.getConfiguration()
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CommandButton("Get Firmware version", modifier, viewModel.isConfirmed) {
                        requestService.getFirmwareVersion()
                    }

                    Spacer(modifier = modifier)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
                            .padding(12.dp)
                ) {
                    Column {
                        Text("Set light", style = MaterialTheme.typography.h6)

                        SetLightRow(
                            label = "Relais 1",
                            enabled = viewModel.isConfirmed,
                            onGreen = { requestService.setLightRequest(1, true) },
                            onRed = { requestService.setLightRequest(1, false) },
                        )
                        SetLightRow(
                            label = "Relais 2",
                            enabled = viewModel.isConfirmed,
                            onGreen = { requestService.setLightRequest(2, true) },
                            onRed = { requestService.setLightRequest(2, false) },
                        )
                        SetLightRow(
                            label = "Relais 3",
                            enabled = viewModel.isConfirmed,
                            onGreen = { requestService.setLightRequest(3, true) },
                            onRed = { requestService.setLightRequest(3, false) },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            requestService.sendJsonCommands(file.readBytes())
                        }
                    },
                    enabled = viewModel.isConfirmed,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Send JSON message")
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier =
                        Modifier.weight(1f)
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                            .verticalScroll(scrollState)
                ) {
                    Text(Logger.loggingText, fontSize = 14.sp)
                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { viewModel.isCommunicationEnabled = !viewModel.isCommunicationEnabled },
                        modifier = Modifier.padding(end = 8.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                backgroundColor = if (viewModel.isCommunicationEnabled) Color.Green else Color.Red
                            ),
                    ) {
                        Text(
                            text =
                                if (viewModel.isCommunicationEnabled) "Activate communication interruptions"
                                else "Deactivate communication interruptions",
                            color = Color.Black,
                        )
                    }
                    Button(onClick = { Logger.exportLogFile() }, modifier = Modifier.padding(end = 8.dp)) {
                        Text("Save logging to file")
                    }
                    Button(onClick = { Logger.loggingText = "" }) { Text("Clear logging") }
                }
            }
        }
    }
}
