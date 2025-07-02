// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package nl.alliander.oslp.components

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun KeyUploadButton(label: String, filePath: String, onUploadClick: () -> Unit) {
    val buttonColor = if (filePath.isNotEmpty()) Color(0xFF4CAF50) else Color(0xFFF44336)

    Button(
        onClick = onUploadClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor, contentColor = Color.White),
    ) {
        Text(label)
    }
}
