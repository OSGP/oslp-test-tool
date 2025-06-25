package nl.alliander.oslp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SetLightRow(label: String, enabled: Boolean, onGreen: () -> Unit, onRed: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(label, modifier = Modifier.width(70.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onGreen,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            enabled = enabled
        ) {
            Text("On", color = Color.White)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onRed,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            enabled = enabled
        ) {
            Text("Off", color = Color.White)
        }
    }
}
