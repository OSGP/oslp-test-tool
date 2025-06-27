package nl.alliander.oslp.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object LocationConfiguration {
    var latitude by mutableStateOf(52260857)
    var longitude by mutableStateOf(5263121)

    fun validLocationConfiguration() = latitude > 0 && longitude > 0
}