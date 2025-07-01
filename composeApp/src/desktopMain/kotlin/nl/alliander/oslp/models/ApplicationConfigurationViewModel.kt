// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package nl.alliander.oslp.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ApplicationConfigurationViewModel {
    var latitude: Int by mutableStateOf(52260857)
    var longitude: Int by mutableStateOf(5263121)
    var clientAddress: String by mutableStateOf("localhost")
    var clientPort: Int by mutableStateOf(12124)
    var serverSocketAddress: String by mutableStateOf("localhost")
    var serverSocketPort: Int by mutableStateOf(12122)
    var privateKeyPath: String by mutableStateOf("")
    var publicKeyPath: String by mutableStateOf("")

    fun validLocationConfiguration(): Boolean = latitude > 0 && longitude > 0

    fun validConnectionConfiguration(): Boolean =
        clientAddress.isNotEmpty() && clientPort > 0 && serverSocketAddress.isNotEmpty() && serverSocketPort > 0
}
