// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.models

import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ApplicationConfiguration(
    val latitude: Int = 52260857,
    val longitude: Int = 5263121,
    val clientAddress: String = "localhost",
    val clientPort: Int = 12124,
    val serverSocketAddress: String = "localhost",
    val serverSocketPort: Int = 12122,
    val privateKeyPath: String = "",
    val publicKeyPath: String = "",
) {

    private constructor(
        applicationConfigurationViewModel: ApplicationConfigurationViewModel
    ) : this(
        applicationConfigurationViewModel.latitude,
        applicationConfigurationViewModel.longitude,
        applicationConfigurationViewModel.clientAddress,
        applicationConfigurationViewModel.clientPort,
        applicationConfigurationViewModel.serverSocketAddress,
        applicationConfigurationViewModel.serverSocketPort,
        applicationConfigurationViewModel.privateKeyPath,
        applicationConfigurationViewModel.publicKeyPath,
    )

    fun toModel(): ApplicationConfigurationViewModel {
        val model = ApplicationConfigurationViewModel()
        model.latitude = latitude
        model.longitude = longitude
        model.clientAddress = clientAddress
        model.clientPort = clientPort
        model.serverSocketAddress = serverSocketAddress
        model.serverSocketPort = serverSocketPort
        model.privateKeyPath = privateKeyPath
        model.publicKeyPath = publicKeyPath
        return model
    }

    fun storeConfiguration() {
        val file = File(CONFIG_FILE_NAME)
        file.writeText(Json.encodeToString(this))
    }

    companion object {
        private var instance: ApplicationConfiguration = loadConfiguration() ?: ApplicationConfiguration()

        fun get(): ApplicationConfiguration = instance

        fun update(applicationConfigurationViewModel: ApplicationConfigurationViewModel): ApplicationConfiguration {
            instance = ApplicationConfiguration(applicationConfigurationViewModel)
            return instance
        }

        private fun loadConfiguration(): ApplicationConfiguration? {
            val file = File(CONFIG_FILE_NAME)
            if (file.exists()) {
                return Json.decodeFromString<ApplicationConfiguration>(file.readText())
            }

            return null
        }

        private const val CONFIG_FILE_NAME = "app_config.json"
    }
}
