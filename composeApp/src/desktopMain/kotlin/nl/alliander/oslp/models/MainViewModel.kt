package nl.alliander.oslp.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainViewModel {
    var isDeviceRegistered by mutableStateOf(false)
    var isConfirmed by mutableStateOf(false)
}
