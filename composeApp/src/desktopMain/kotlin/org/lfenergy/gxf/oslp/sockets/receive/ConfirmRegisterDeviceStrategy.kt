// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.sockets.receive

import org.lfenergy.gxf.oslp.domain.Envelope
import org.lfenergy.gxf.oslp.service.DeviceStateService
import org.lfenergy.gxf.oslp.util.Logger
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.Message

class ConfirmRegisterDeviceStrategy : ReceiveStrategy() {
    private val deviceStateService = DeviceStateService.getInstance()

    override fun matches(message: Message): Boolean = message.hasConfirmRegisterDeviceRequest()

    override fun handle(requestEnvelope: Envelope) {

        with(requestEnvelope.message.confirmRegisterDeviceRequest) {
            if (randomDevice != deviceStateService.randomDevice)
                Logger.logReceive(
                    "Invalid randomDevice! Expected: ${deviceStateService.randomDevice} - Got: $randomDevice"
                )
            if (randomPlatform != deviceStateService.randomPlatform)
                Logger.logReceive(
                    "Invalid randomPlatform! Expected: ${deviceStateService.randomPlatform} - Got: $randomPlatform"
                )
        }

        deviceStateService.confirmRegisterDevice(requestEnvelope.sequenceNumber)
    }

    override fun buildResponsePayload(requestEnvelope: Envelope): Message {
        val response =
            Message.newBuilder()
                .setConfirmRegisterDeviceResponse(
                    Oslp.ConfirmRegisterDeviceResponse.newBuilder()
                        .setRandomDevice(deviceStateService.randomDevice)
                        .setRandomPlatform(deviceStateService.randomPlatform)
                        .setSequenceWindow(1)
                        .setStatusValue(0)
                        .build()
                )
                .build()
        return response
    }
}
