/*
 * Copyright 2025 Alliander N.V.
 */
package nl.alliander.oslp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import javax.swing.JFileChooser
import nl.alliander.oslp.components.KeyUploadButton
import nl.alliander.oslp.models.ConnectionConfiguration
import nl.alliander.oslp.models.KeyConfiguration
import nl.alliander.oslp.models.LocationConfiguration

@Composable
fun ConfigurationScreen(onContinue: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Configuration", style = MaterialTheme.typography.h6)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = ConnectionConfiguration.serverSocketAddress,
                    onValueChange = { ConnectionConfiguration.serverSocketAddress = it },
                    label = { Text("Test tool address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = ConnectionConfiguration.serverSocketPort.toString(),
                    onValueChange = { ConnectionConfiguration.serverSocketPort = it.toIntOrNull() ?: 0 },
                    label = { Text("Test tool port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = ConnectionConfiguration.clientAddress,
                    onValueChange = { ConnectionConfiguration.clientAddress = it },
                    label = { Text("Device address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = ConnectionConfiguration.clientPort.toString(),
                    onValueChange = { ConnectionConfiguration.clientPort = it.toIntOrNull() ?: 0 },
                    label = { Text("Device port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = LocationConfiguration.latitude.toString(),
                    onValueChange = { LocationConfiguration.latitude = it.toIntOrNull() ?: 0 },
                    label = { Text("Latitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = LocationConfiguration.longitude.toString(),
                    onValueChange = { LocationConfiguration.longitude = it.toIntOrNull() ?: 0 },
                    label = { Text("Longitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                KeyUploadButton(
                    label = "Upload Private Key",
                    uploadedBytes = KeyConfiguration.privateKeyBytes,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            val bytes = file.readBytes()
                            KeyConfiguration.privateKeyBytes = bytes
                        }
                    },
                )

                KeyUploadButton(
                    label = "Upload Public Key",
                    uploadedBytes = KeyConfiguration.publicKeyBytes,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            val bytes = file.readBytes()
                            KeyConfiguration.publicKeyBytes = bytes
                        }
                    },
                )
            }

            Button(
                onClick = onContinue,
                enabled =
                    KeyConfiguration.validKeys() &&
                        ConnectionConfiguration.validConnectionConfiguration() &&
                        LocationConfiguration.validLocationConfiguration(),
            ) {
                Text("Continue")
            }
        }
    }
}
