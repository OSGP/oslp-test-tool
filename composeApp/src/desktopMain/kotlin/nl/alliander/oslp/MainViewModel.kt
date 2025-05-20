package nl.alliander.oslp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainViewModel {
    var loggingText by mutableStateOf("")
    var isDeviceRegistered by mutableStateOf(false)
    var isConfirmed by mutableStateOf(false)

    fun appendLog(text: String) {
        loggingText += "$text\n"
    }
}
