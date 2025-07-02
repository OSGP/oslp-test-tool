// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.sockets.receive

import org.lfenergy.gxf.oslp.domain.Envelope
import org.lfenergy.gxf.oslp.util.Logger
import org.lfenergy.gxf.oslp.util.SigningUtil
import org.lfenergy.gxf.oslp.util.toByteArray
import org.opensmartgridplatform.oslp.Oslp.Message

abstract class ReceiveStrategy {
    abstract fun matches(message: Message): Boolean

    abstract fun handle(requestEnvelope: Envelope)

    abstract fun buildResponsePayload(requestEnvelope: Envelope): Message

    operator fun invoke(requestEnvelope: Envelope): Envelope? {
        if (!validateSignature(requestEnvelope)) return null
        handle(requestEnvelope)
        val responsePayload = buildResponsePayload(requestEnvelope).toByteArray()
        return createResponseEnvelope(requestEnvelope, responsePayload)
    }

    private fun validateSignature(requestEnvelope: Envelope): Boolean {
        val verified =
            with(requestEnvelope) {
                SigningUtil.verifySignature(
                    sequenceNumber.toByteArray(2) + deviceId + lengthIndicator.toByteArray(2) + messageBytes,
                    securityKey,
                )
            }

        if (!verified) {
            Logger.logReceive("The signature is not valid!")
            return false
        }
        return true
    }

    private fun createResponseEnvelope(requestEnvelope: Envelope, responsePayload: ByteArray): Envelope {
        val securityKey =
            SigningUtil.createSignature(
                requestEnvelope.sequenceNumber.toByteArray(2) +
                    requestEnvelope.deviceId +
                    responsePayload.size.toByteArray(2) +
                    responsePayload
            )

        return Envelope(
            securityKey,
            requestEnvelope.sequenceNumber,
            requestEnvelope.deviceId,
            responsePayload.size,
            responsePayload,
        )
    }

    companion object {
        fun getStrategyFor(message: Message): ReceiveStrategy {
            with(message) {
                return when {
                    hasRegisterDeviceRequest() -> RegisterDeviceStrategy()
                    hasConfirmRegisterDeviceRequest() -> ConfirmRegisterDeviceStrategy()
                    hasEventNotificationRequest() -> EventNotificationRequestStrategy()
                    else -> error("Unexpected request message: $message")
                }
            }
        }
    }
}
