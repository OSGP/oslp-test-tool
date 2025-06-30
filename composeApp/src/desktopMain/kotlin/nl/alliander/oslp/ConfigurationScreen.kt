// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
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
import nl.alliander.oslp.models.ApplicationConfiguration

@Composable
fun ConfigurationScreen(onContinue: () -> Unit) {
    val applicationConfiguration = ApplicationConfiguration.getInstance()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Configuration", style = MaterialTheme.typography.h6)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = applicationConfiguration.serverSocketAddress,
                    onValueChange = { applicationConfiguration.serverSocketAddress = it },
                    label = { Text("Test tool address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = applicationConfiguration.serverSocketPort.toString(),
                    onValueChange = { applicationConfiguration.serverSocketPort = it.toIntOrNull() ?: 0 },
                    label = { Text("Test tool port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = applicationConfiguration.clientAddress,
                    onValueChange = { applicationConfiguration.clientAddress = it },
                    label = { Text("Device address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = applicationConfiguration.clientPort.toString(),
                    onValueChange = { applicationConfiguration.clientPort = it.toIntOrNull() ?: 0 },
                    label = { Text("Device port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = applicationConfiguration.latitude.toString(),
                    onValueChange = { applicationConfiguration.latitude = it.toIntOrNull() ?: 0 },
                    label = { Text("Latitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = applicationConfiguration.longitude.toString(),
                    onValueChange = { applicationConfiguration.longitude = it.toIntOrNull() ?: 0 },
                    label = { Text("Longitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                KeyUploadButton(
                    label = "Upload Private Key",
                    keyUploaded = ApplicationConfiguration.privateKeyUploaded,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            applicationConfiguration.privateKeyPath = file.path
                            ApplicationConfiguration.privateKeyUploaded = true
                        }
                    },
                )

                KeyUploadButton(
                    label = "Upload Public Key",
                    keyUploaded = ApplicationConfiguration.publicKeyUploaded,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            applicationConfiguration.publicKeyPath = file.path
                            ApplicationConfiguration.publicKeyUploaded = true
                        }
                    },
                )
            }

            Button(
                onClick = onContinue,
                enabled =
                    applicationConfiguration.validConnectionConfiguration() &&
                            applicationConfiguration.validLocationConfiguration()
            ) {
                Text("Continue")
            }
        }
    }
}
