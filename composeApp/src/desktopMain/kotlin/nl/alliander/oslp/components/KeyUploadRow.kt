package nl.alliander.oslp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
fun KeyUploadRow(
    label: String,
    uploadedBytes: ByteArray?,
    onUploadClick: () -> Unit
) {
    val buttonColor = if (uploadedBytes != null) Color(0xFF4CAF50) else Color(0xFFF44336)

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onUploadClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = buttonColor,
                contentColor = Color.White
            )
        ) {
            Text(label)
        }

        val sizeText = uploadedBytes?.size?.let { "$it bytes" } ?: " "
        Text(
            sizeText,
            modifier = Modifier.width(100.dp),
            color = Color.Gray
        )
    }
}



