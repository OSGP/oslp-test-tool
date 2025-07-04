// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp

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
import org.lfenergy.gxf.oslp.components.KeyUploadButton
import org.lfenergy.gxf.oslp.models.ApplicationConfigurationViewModel

@Composable
fun ConfigurationScreen(viewModel: ApplicationConfigurationViewModel, onContinue: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Configuration", style = MaterialTheme.typography.h6)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = viewModel.serverSocketAddress,
                    onValueChange = { viewModel.serverSocketAddress = it },
                    label = { Text("Test tool address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = viewModel.serverSocketPort.toString(),
                    onValueChange = { viewModel.serverSocketPort = it.toIntOrNull() ?: 0 },
                    label = { Text("Test tool port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = viewModel.clientAddress,
                    onValueChange = { viewModel.clientAddress = it },
                    label = { Text("Device address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = viewModel.clientPort.toString(),
                    onValueChange = { viewModel.clientPort = it.toIntOrNull() ?: 0 },
                    label = { Text("Device port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = viewModel.latitude.toString(),
                    onValueChange = { viewModel.latitude = it.toIntOrNull() ?: 0 },
                    label = { Text("Latitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
                TextField(
                    value = viewModel.longitude.toString(),
                    onValueChange = { viewModel.longitude = it.toIntOrNull() ?: 0 },
                    label = { Text("Longitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                KeyUploadButton(
                    label = "Upload Private Key",
                    filePath = viewModel.privateKeyPath,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            viewModel.privateKeyPath = file.path
                        }
                    },
                )

                KeyUploadButton(
                    label = "Upload Public Key",
                    filePath = viewModel.publicKeyPath,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            viewModel.publicKeyPath = file.path
                        }
                    },
                )
            }

            Button(
                onClick = onContinue,
                enabled = viewModel.validConnectionConfiguration() && viewModel.validLocationConfiguration(),
            ) {
                Text("Continue")
            }
        }
    }
}
