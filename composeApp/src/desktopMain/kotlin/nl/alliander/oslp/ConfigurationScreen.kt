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
import nl.alliander.oslp.components.KeyUploadRow
import nl.alliander.oslp.models.ConfigurationModel
import javax.swing.JFileChooser

@Composable
fun ConfigurationScreen(
    onContinue: () -> Unit,
    configurationModel: ConfigurationModel
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
                    value = configurationModel.serverSocketAddress,
                    onValueChange = { configurationModel.serverSocketAddress = it },
                    label = { Text("Test tool address") },
                    singleLine = true
                )
                TextField(
                    value = configurationModel.serverSocketPort.toString(),
                    onValueChange = { configurationModel.serverSocketPort = it.toInt() },
                    label = { Text("Test tool port") },
                    singleLine = true,
                    modifier = Modifier.width(150.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = configurationModel.clientAddress,
                    onValueChange = { configurationModel.clientAddress = it },
                    label = { Text("Device address") },
                    singleLine = true
                )
                TextField(
                    value = configurationModel.clientPort.toString(),
                    onValueChange = { configurationModel.clientPort = it.toInt() },
                    label = { Text("Device port") },
                    singleLine = true,
                    modifier = Modifier.width(150.dp)
                )
            }

            KeyUploadRow(
                label = "Upload Private Key",
                uploadedBytes = configurationModel.keys.privateKeyBytes,
                onUploadClick = {
                    val fileChooser = JFileChooser()
                    val result = fileChooser.showOpenDialog(null)
                    if (result == JFileChooser.APPROVE_OPTION) {
                        val file = fileChooser.selectedFile
                        val bytes = file.readBytes()
                        configurationModel.keys.privateKeyBytes = bytes
                    }
                }
            )

            KeyUploadRow(
                label = "Upload Public Key",
                uploadedBytes = configurationModel.keys.publicKeyBytes,
                onUploadClick = {
                    val fileChooser = JFileChooser()
                    val result = fileChooser.showOpenDialog(null)
                    if (result == JFileChooser.APPROVE_OPTION) {
                        val file = fileChooser.selectedFile
                        val bytes = file.readBytes()
                        configurationModel.keys.publicKeyBytes = bytes
                    }
                }
            )

            Button(onClick = onContinue) {
                Text("Continue")
            }
        }
    }
}
