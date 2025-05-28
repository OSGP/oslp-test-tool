package nl.alliander.oslp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.alliander.oslp.components.CircleIndicator
import nl.alliander.oslp.components.CommandButton
import nl.alliander.oslp.models.MainViewModel
import nl.alliander.oslp.components.SetLightRow
import nl.alliander.oslp.service.LoggingService

@Composable
@androidx.compose.desktop.ui.tooling.preview.Preview
fun App(loggingService: LoggingService) {

    val viewModel = remember { MainViewModel() }

    val scrollState = rememberScrollState()
    LaunchedEffect(loggingService.loggingText) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

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

                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CommandButton("Selftest (10 sec)", modifier) { loggingService.appendLog("Selftest (10 sec) clicked") }
                    CommandButton("Reboot", modifier) { loggingService.appendLog("Reboot clicked") }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CommandButton("Light sensor on", modifier) { loggingService.appendLog("Light sensor on (Light) clicked") }
                    CommandButton("Light sensor off", modifier) { loggingService.appendLog("Light sensor off (Dark) clicked") }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CommandButton("Get Status", modifier) { loggingService.appendLog("Get Status clicked") }
                    CommandButton("Get Configuration", modifier) { loggingService.appendLog("Get Configuration clicked") }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CommandButton("Get Firmware versie", modifier) { loggingService.appendLog("Get Firmware versie clicked") }

                    Spacer(modifier = modifier)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
                        .padding(12.dp)
                ) {
                    Column {
                        Text("Set light", style = MaterialTheme.typography.h6)

                        SetLightRow(
                            label = "Relais 1",
                            onGreen = { loggingService.appendLog("Relais 1 ON") },
                            onRed = { loggingService.appendLog("Relais 1 OFF") }
                        )
                        SetLightRow(
                            label = "Relais 2",
                            onGreen = { loggingService.appendLog("Relais 2 ON") },
                            onRed = { loggingService.appendLog("Relais 2 OFF") }
                        )
                        SetLightRow(
                            label = "Relais 3",
                            onGreen = { loggingService.appendLog("Relais 3 ON") },
                            onRed = { loggingService.appendLog("Relais 3 OFF") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { loggingService.appendLog("Send JSON message clicked") },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Send JSON message")
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(loggingService.loggingText, fontSize = 14.sp)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { loggingService.exportLogFile() },
                        modifier = Modifier
                            .padding(end = 8.dp)
                    ) {
                        Text("Save logging to file")
                    }
                    Button(
                        onClick = { loggingService.loggingText = "" }
                    ) {
                        Text("Clear logging")
                    }
                }
            }
        }
    }
}
