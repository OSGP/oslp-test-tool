/*
 * Copyright 2025 Alliander N.V.
 */
package nl.alliander.oslp

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.alliander.oslp.components.circleIndicator
import nl.alliander.oslp.components.commandButton
import nl.alliander.oslp.components.setLightRow
import nl.alliander.oslp.models.MainViewModel
import nl.alliander.oslp.service.RequestService
import nl.alliander.oslp.util.Logger

@Composable
@androidx.compose.desktop.ui.tooling.preview.Preview
fun app(
    requestService: RequestService,
    viewModel: MainViewModel,
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Logger.loggingText) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    MaterialTheme {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val modifier = Modifier.weight(1f).height(60.dp)
            Column(modifier = Modifier.width(300.dp).padding(end = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    circleIndicator(viewModel.isDeviceRegistered, "Register device")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    circleIndicator(viewModel.isConfirmed, "Confirm registration")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    commandButton("Selftest (10 sec)", modifier) { Logger.log("Selftest (10 sec) clicked") }
                    commandButton("Reboot", modifier) { Logger.log("Reboot clicked") }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    commandButton("Light sensor on", modifier) { Logger.log("Light sensor on (Light) clicked") }
                    commandButton(
                        "Light sensor off",
                        modifier,
                    ) { Logger.log("Light sensor off (Dark) clicked") }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    commandButton("Get Status", modifier) { Logger.log("Get Status clicked") }
                    commandButton("Get Configuration", modifier) { Logger.log("Get Configuration clicked") }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    commandButton("Get Firmware versie", modifier) { requestService.getFirmwareVersion() }

                    Spacer(modifier = modifier)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
                            .padding(12.dp),
                ) {
                    Column {
                        Text("Set light", style = MaterialTheme.typography.h6)

                        setLightRow(
                            label = "Relais 1",
                            onGreen = { Logger.log("Relais 1 ON") },
                            onRed = { Logger.log("Relais 1 OFF") },
                        )
                        setLightRow(
                            label = "Relais 2",
                            onGreen = { Logger.log("Relais 2 ON") },
                            onRed = { Logger.log("Relais 2 OFF") },
                        )
                        setLightRow(
                            label = "Relais 3",
                            onGreen = { Logger.log("Relais 3 ON") },
                            onRed = { Logger.log("Relais 3 OFF") },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { Logger.log("Send JSON message clicked") },
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    Text("Send JSON message")
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                            .verticalScroll(scrollState),
                ) {
                    Text(Logger.loggingText, fontSize = 14.sp)
                }

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(
                        onClick = { Logger.exportLogFile() },
                        modifier =
                            Modifier
                                .padding(end = 8.dp),
                    ) {
                        Text("Save logging to file")
                    }
                    Button(
                        onClick = { Logger.loggingText = "" },
                    ) {
                        Text("Clear logging")
                    }
                }
            }
        }
    }
}
