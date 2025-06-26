package nl.alliander.oslp.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class PortConfigurationModel {
    var clientAddress by mutableStateOf("localhost")
    var clientPort by mutableStateOf(12124)
    var serverSocketAddress by mutableStateOf("localhost")
    var serverSocketPort by mutableStateOf(12122)
    var privateKeyBytes by mutableStateOf<ByteArray?>(null)
    var publicKeyBytes by mutableStateOf<ByteArray?>(null)
}