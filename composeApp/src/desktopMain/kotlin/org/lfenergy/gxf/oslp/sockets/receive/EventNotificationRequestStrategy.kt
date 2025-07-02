// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.sockets.receive

import org.lfenergy.gxf.oslp.domain.Envelope
import org.lfenergy.gxf.oslp.service.DeviceStateService
import org.lfenergy.gxf.oslp.util.Logger
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.Message

class EventNotificationRequestStrategy : ReceiveStrategy() {
    private val deviceStateService = DeviceStateService.getInstance()

    override fun matches(message: Message): Boolean = message.hasEventNotificationRequest()

    override fun handle(requestEnvelope: Envelope) {
        deviceStateService.updateSequenceNumber(requestEnvelope.sequenceNumber)
        Logger.logReceive("Received event notification request: ${requestEnvelope.message.eventNotificationRequest}")
    }

    override fun buildResponsePayload(requestEnvelope: Envelope): Message {
        return Message.newBuilder()
            .setEventNotificationResponse(Oslp.EventNotificationResponse.newBuilder().setStatus(Oslp.Status.OK))
            .build()
    }
}
