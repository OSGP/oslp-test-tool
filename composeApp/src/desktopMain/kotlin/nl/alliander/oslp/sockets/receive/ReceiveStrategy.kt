package nl.alliander.oslp.sockets.receive

import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.util.Logger
import nl.alliander.oslp.util.SigningUtil
import nl.alliander.oslp.util.toByteArray
import org.opensmartgridplatform.oslp.Oslp.Message

abstract class ReceiveStrategy {
    abstract fun matches(message: Message): Boolean

    abstract fun handle(requestEnvelope: Envelope)

    abstract fun buildResponsePayload(requestEnvelope: Envelope): Message

    operator fun invoke(requestEnvelope: Envelope): Envelope {

        val verified = with(requestEnvelope) {
            SigningUtil.verifySignature(
                sequenceNumber.toByteArray(2) +
                        deviceId +
                        lengthIndicator.toByteArray(2) +
                        messageBytes,
                securityKey,
            )
        }

        if (!verified) {
            Logger.logReceive("Message not verified!")
        }

        handle(requestEnvelope)

        val responsePayload = buildResponsePayload(requestEnvelope).toByteArray()

        val securityKey = with(requestEnvelope) {
            SigningUtil.createSignature(
                sequenceNumber.toByteArray(2) +
                        deviceId +
                        lengthIndicator.toByteArray(2) +
                        messageBytes
            )
        }

        val response = Envelope(
            securityKey,
            requestEnvelope.sequenceNumber,
            requestEnvelope.deviceId,
            responsePayload.size,
            responsePayload
        )

        return response
    }

    companion object {
        fun getStrategyFor(message: Message): ReceiveStrategy {
            with(message) {
                return when {
                    hasRegisterDeviceRequest() -> RegisterDeviceStrategy()
                    hasConfirmRegisterDeviceRequest() -> ConfirmRegisterDeviceStrategy()
                    else -> error("Unexpected request message: $message")
                }
            }
        }
    }
}
