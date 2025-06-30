package nl.alliander.oslp.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class ApplicationConfigurationViewModel {
    var latitude: Int by mutableStateOf(52260857)
    var longitude: Int by mutableStateOf(5263121)
    var clientAddress: String by mutableStateOf("localhost")
    var clientPort: Int by mutableStateOf(12124)
    var serverSocketAddress: String by mutableStateOf("localhost")
    var serverSocketPort: Int by mutableStateOf(12122)
    var privateKeyPath: String by mutableStateOf("")
    var publicKeyPath: String by mutableStateOf("")

    companion object {
        private var instance: ApplicationConfigurationViewModel? = null

        fun getInstance(): ApplicationConfigurationViewModel =
            instance ?: readOrCreateInstance().also { instance = it }

        private fun readOrCreateInstance(): ApplicationConfigurationViewModel {
            val file = File("app_config.json")
            if (file.exists()) {
                return Json.decodeFromString<ApplicationConfiguration>(file.readText()).toModel()
            }

            return ApplicationConfigurationViewModel()
        }
    }

    fun validLocationConfiguration(): Boolean = latitude > 0 && longitude > 0
    fun validConnectionConfiguration(): Boolean =
        clientAddress.isNotEmpty() && clientPort > 0 && serverSocketAddress.isNotEmpty() && serverSocketPort > 0
}

