// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package nl.alliander.oslp.sockets.receive

import kotlin.random.Random
import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.models.ApplicationConfiguration
import nl.alliander.oslp.service.DeviceStateService
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.Message

class RegisterDeviceStrategy : ReceiveStrategy() {
    override fun matches(message: Message): Boolean = message.hasRegisterDeviceRequest()

    override fun handle(requestEnvelope: Envelope) {
        val deviceStateService = DeviceStateService.getInstance()
        deviceStateService.registerDevice(requestEnvelope.deviceId)
        deviceStateService.randomDevice = requestEnvelope.message.registerDeviceRequest.randomDevice
    }

    override fun buildResponsePayload(requestEnvelope: Envelope): Message {
        val deviceStateService = DeviceStateService.getInstance()
        val applicationConfiguration = ApplicationConfiguration.getInstance()

        deviceStateService.deviceId = requestEnvelope.deviceId
        deviceStateService.randomPlatform = Random.nextInt(65536)

        val response =
            Message.newBuilder()
                .setRegisterDeviceResponse(
                    Oslp.RegisterDeviceResponse.newBuilder()
                        .setRandomDevice(requestEnvelope.message.registerDeviceRequest.randomDevice)
                        .setCurrentTime(System.currentTimeMillis().toString())
                        .setStatus(Oslp.Status.OK)
                        .setRandomPlatform(deviceStateService.randomPlatform)
                        .setLocationInfo(
                            Oslp.LocationInfo.newBuilder()
                                .setLatitude(applicationConfiguration.latitude)
                                .setLongitude(applicationConfiguration.longitude)
                                .setTimeOffset(60)
                        )
                        .build()
                )
                .build()

        return response
    }
}
