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
import nl.alliander.oslp.models.ApplicationConfigurationViewModel

@Composable
fun ConfigurationScreen(onContinue: () -> Unit) {
    val applicationConfigurationViewModel = ApplicationConfigurationViewModel.getInstance()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Configuration", style = MaterialTheme.typography.h6)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = applicationConfigurationViewModel.serverSocketAddress,
                    onValueChange = { applicationConfigurationViewModel.serverSocketAddress = it },
                    label = { Text("Test tool address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = applicationConfigurationViewModel.serverSocketPort.toString(),
                    onValueChange = { applicationConfigurationViewModel.serverSocketPort = it.toIntOrNull() ?: 0 },
                    label = { Text("Test tool port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = applicationConfigurationViewModel.clientAddress,
                    onValueChange = { applicationConfigurationViewModel.clientAddress = it },
                    label = { Text("Device address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = applicationConfigurationViewModel.clientPort.toString(),
                    onValueChange = { applicationConfigurationViewModel.clientPort = it.toIntOrNull() ?: 0 },
                    label = { Text("Device port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = applicationConfigurationViewModel.latitude.toString(),
                    onValueChange = { applicationConfigurationViewModel.latitude = it.toIntOrNull() ?: 0 },
                    label = { Text("Latitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = applicationConfigurationViewModel.longitude.toString(),
                    onValueChange = { applicationConfigurationViewModel.longitude = it.toIntOrNull() ?: 0 },
                    label = { Text("Longitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                KeyUploadButton(
                    label = "Upload Private Key",
                    filePath = applicationConfigurationViewModel.privateKeyPath,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            applicationConfigurationViewModel.privateKeyPath = file.path
                        }
                    },
                )

                KeyUploadButton(
                    label = "Upload Public Key",
                    filePath = applicationConfigurationViewModel.publicKeyPath,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            applicationConfigurationViewModel.publicKeyPath = file.path
                        }
                    },
                )
            }

            Button(
                onClick = onContinue,
                enabled =
                    applicationConfigurationViewModel.validConnectionConfiguration() &&
                            applicationConfigurationViewModel.validLocationConfiguration()
            ) {
                Text("Continue")
            }
        }
    }
}
