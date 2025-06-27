package nl.alliander.oslp.sockets.receive

import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.models.KeyConfiguration
import nl.alliander.oslp.models.LocationConfiguration
import nl.alliander.oslp.util.Logger
import nl.alliander.oslp.util.SigningUtil
import nl.alliander.oslp.util.toByteArray
import org.opensmartgridplatform.oslp.Oslp.Message

abstract class ReceiveStrategy {
    abstract fun matches(message: Message): Boolean

    abstract fun handle(requestEnvelope: Envelope)

    abstract fun buildResponsePayload(requestEnvelope: Envelope, locationConfiguration: LocationConfiguration): Message

    operator fun invoke(
        requestEnvelope: Envelope,
        keys: KeyConfiguration,
        locationConfiguration: LocationConfiguration
    ): Envelope? {
        if (!validateSignature(requestEnvelope, keys)) return null
        handle(requestEnvelope)
        val responsePayload = buildResponsePayload(requestEnvelope, locationConfiguration).toByteArray()
        return createResponseEnvelope(requestEnvelope, responsePayload, keys)
    }

    private fun validateSignature(requestEnvelope: Envelope, keys: KeyConfiguration): Boolean {
        val verified = with(requestEnvelope) {
            SigningUtil.verifySignature(
                sequenceNumber.toByteArray(2) +
                        deviceId +
                        lengthIndicator.toByteArray(2) +
                        messageBytes,
                securityKey,
                keys.publicKey ?: Logger.logAndThrowError("Missing public key")
            )
        }

        if (!verified) {
            Logger.logReceive("The signature is not valid!")
            return false
        }
        return true
    }

    private fun createResponseEnvelope(
        requestEnvelope: Envelope,
        responsePayload: ByteArray,
        keys: KeyConfiguration
    ): Envelope {
        val securityKey = SigningUtil.createSignature(
            requestEnvelope.sequenceNumber.toByteArray(2) +
                    requestEnvelope.deviceId +
                    responsePayload.size.toByteArray(2) +
                    responsePayload,
            keys.privateKey ?: Logger.logAndThrowError("Missing private key")
        )

        return Envelope(
            securityKey,
            requestEnvelope.sequenceNumber,
            requestEnvelope.deviceId,
            responsePayload.size,
            responsePayload
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
