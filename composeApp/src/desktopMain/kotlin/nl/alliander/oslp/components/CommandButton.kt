/*
 * Copyright 2025 Alliander N.V.
 */
package nl.alliander.oslp.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@androidx.compose.desktop.ui.tooling.preview.Preview
fun commandButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
    ) {
        Text(text = text)
    }
}
