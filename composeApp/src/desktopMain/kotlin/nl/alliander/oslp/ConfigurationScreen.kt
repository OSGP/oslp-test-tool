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
import nl.alliander.oslp.components.KeyUploadButton
import nl.alliander.oslp.models.AppConfiguration
import javax.swing.JFileChooser

@Composable
fun ConfigurationScreen(
    onContinue: () -> Unit,
    appConfiguration: AppConfiguration
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Configuration", style = MaterialTheme.typography.h6)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = appConfiguration.connectionConfiguration.serverSocketAddress,
                    onValueChange = { appConfiguration.connectionConfiguration.serverSocketAddress = it },
                    label = { Text("Test tool address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp)
                )
                TextField(
                    value = appConfiguration.connectionConfiguration.serverSocketPort.toString(),
                    onValueChange = { appConfiguration.connectionConfiguration.serverSocketPort = it.toInt() },
                    label = { Text("Test tool port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = appConfiguration.connectionConfiguration.clientAddress,
                    onValueChange = { appConfiguration.connectionConfiguration.clientAddress = it },
                    label = { Text("Device address") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp)
                )
                TextField(
                    value = appConfiguration.connectionConfiguration.clientPort.toString(),
                    onValueChange = { appConfiguration.connectionConfiguration.clientPort = it.toInt() },
                    label = { Text("Device port") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = appConfiguration.locationConfiguration.latitude.toString(),
                    onValueChange = { appConfiguration.locationConfiguration.latitude = it.toInt() },
                    label = { Text("Latitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp)
                )
                TextField(
                    value = appConfiguration.locationConfiguration.longitude.toString(),
                    onValueChange = { appConfiguration.locationConfiguration.longitude = it.toInt() },
                    label = { Text("Longitude") },
                    singleLine = true,
                    modifier = Modifier.width(250.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                KeyUploadButton(
                    label = "Upload Private Key",
                    uploadedBytes = appConfiguration.keys.privateKeyBytes,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            val bytes = file.readBytes()
                            appConfiguration.keys.privateKeyBytes = bytes
                        }
                    }
                )

                KeyUploadButton(
                    label = "Upload Public Key",
                    uploadedBytes = appConfiguration.keys.publicKeyBytes,
                    onUploadClick = {
                        val fileChooser = JFileChooser()
                        val result = fileChooser.showOpenDialog(null)
                        if (result == JFileChooser.APPROVE_OPTION) {
                            val file = fileChooser.selectedFile
                            val bytes = file.readBytes()
                            appConfiguration.keys.publicKeyBytes = bytes
                        }
                    }
                )
            }

            Button(onClick = onContinue) {
                Text("Continue")
            }
        }
    }
}
