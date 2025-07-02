// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@androidx.compose.desktop.ui.tooling.preview.Preview
fun CommandButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = false, onClick: () -> Unit) {
    Button(onClick = { onClick() }, modifier = modifier, enabled = enabled) { Text(text = text) }
}
