// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package nl.alliander.oslp.models

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationConfiguration(
    val latitude: Int,
    val longitude: Int,
    val clientAddress: String,
    val clientPort: Int,
    val serverSocketAddress: String,
    val serverSocketPort: Int,
    val privateKeyPath: String,
    val publicKeyPath: String,
) {

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

    companion object {
        fun fromModel(applicationConfigurationViewModel: ApplicationConfigurationViewModel): ApplicationConfiguration =
            ApplicationConfiguration(
                applicationConfigurationViewModel.latitude,
                applicationConfigurationViewModel.longitude,
                applicationConfigurationViewModel.clientAddress,
                applicationConfigurationViewModel.clientPort,
                applicationConfigurationViewModel.serverSocketAddress,
                applicationConfigurationViewModel.serverSocketPort,
                applicationConfigurationViewModel.privateKeyPath,
                applicationConfigurationViewModel.publicKeyPath,
            )
    }
}
