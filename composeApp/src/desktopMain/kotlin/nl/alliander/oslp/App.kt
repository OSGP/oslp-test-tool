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
import nl.alliander.oslp.MainViewModel
import nl.alliander.oslp.components.SetLightRow

@Composable
@androidx.compose.desktop.ui.tooling.preview.Preview
fun App(viewModel: MainViewModel = remember { MainViewModel() }) {
    val scrollState = rememberScrollState()
    LaunchedEffect(viewModel.loggingText) {
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
                    CommandButton("Selftest (10 sec)", modifier) { viewModel.appendLog("Selftest (10 sec) clicked") }
                    CommandButton("Reboot", modifier) { viewModel.appendLog("Reboot clicked") }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CommandButton("Light sensor on", modifier) { viewModel.appendLog("Light sensor on (Light) clicked") }
                    CommandButton("Light sensor off", modifier) { viewModel.appendLog("Light sensor off (Dark) clicked") }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CommandButton("Get Status", modifier) { viewModel.appendLog("Get Status clicked") }
                    CommandButton("Get Configuration", modifier) { viewModel.appendLog("Get Configuration clicked") }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CommandButton("Get Firmware versie", modifier) { viewModel.appendLog("Get Firmware versie clicked") }

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
                            onGreen = { viewModel.appendLog("Relais 1 ON") },
                            onRed = { viewModel.appendLog("Relais 1 OFF") }
                        )
                        SetLightRow(
                            label = "Relais 2",
                            onGreen = { viewModel.appendLog("Relais 2 ON") },
                            onRed = { viewModel.appendLog("Relais 2 OFF") }
                        )
                        SetLightRow(
                            label = "Relais 3",
                            onGreen = { viewModel.appendLog("Relais 3 ON") },
                            onRed = { viewModel.appendLog("Relais 3 OFF") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.appendLog("Send JSON message clicked") },
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
                    Text(viewModel.loggingText, fontSize = 14.sp)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .padding(end = 8.dp)
                    ) {
                        Text("Save logging to file")
                    }
                    Button(
                        onClick = { viewModel.loggingText = "" }
                    ) {
                        Text("Clear logging")
                    }
                }
            }
        }
    }
}
