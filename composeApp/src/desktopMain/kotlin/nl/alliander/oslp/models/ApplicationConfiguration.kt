package nl.alliander.oslp.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.Serializable

data class ApplicationConfiguration(
    var latitude: Int = 52260857,
    var longitude: Int = 5263121,
    var clientAddress: String = "localhost",
    var clientPort: Int = 12124,
    var serverSocketAddress: String = "localhost",
    var serverSocketPort: Int = 12122,
    var privateKeyPath: String = "",
    var publicKeyPath: String = ""
) : Serializable {
    var privateKeyUploaded by mutableStateOf(false)
    var publicKeyUploaded by mutableStateOf(false)

    companion object {
        private var instance: ApplicationConfiguration? = null

        fun getInstance(): ApplicationConfiguration =
            instance ?: readOrCreateInstance().also { instance = it }

        private fun readOrCreateInstance(): ApplicationConfiguration {
            val file = File("app_config")
            if (file.exists()) {
                return ObjectInputStream(FileInputStream(file)).use { input ->
                    input.readObject() as ApplicationConfiguration
                }.also { config ->
                    config.privateKeyUploaded = config.privateKeyPath.isNotEmpty()
                    config.publicKeyUploaded = config.publicKeyPath.isNotEmpty()
                }
            }

            return ApplicationConfiguration()
        }
    }

    fun validLocationConfiguration(): Boolean = latitude > 0 && longitude > 0
    fun validConnectionConfiguration(): Boolean =
        clientAddress.isNotEmpty() && clientPort > 0 && serverSocketAddress.isNotEmpty() && serverSocketPort > 0
}

