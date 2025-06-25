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
import nl.alliander.oslp.models.PortConfigurationModel

@Composable
fun ConfigurationScreen(
    onContinue: () -> Unit,
    portConfigurationModel: PortConfigurationModel
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
                    value = portConfigurationModel.serverSocketAddress,
                    onValueChange = { portConfigurationModel.serverSocketAddress = it },
                    label = { Text("Test tool address") },
                    singleLine = true
                )
                TextField(
                    value = portConfigurationModel.serverSocketPort.toString(),
                    onValueChange = { portConfigurationModel.serverSocketPort = it.toInt() },
                    label = { Text("Test tool port") },
                    singleLine = true,
                    modifier = Modifier.width(150.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = portConfigurationModel.clientAddress,
                    onValueChange = { portConfigurationModel.clientAddress = it },
                    label = { Text("Device address") },
                    singleLine = true
                )
                TextField(
                    value = portConfigurationModel.clientPort.toString(),
                    onValueChange = { portConfigurationModel.clientPort = it.toInt() },
                    label = { Text("Device port") },
                    singleLine = true,
                    modifier = Modifier.width(150.dp)
                )
            }

            Button(onClick = onContinue) {
                Text("Continue")
            }
        }
    }
}