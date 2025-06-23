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

    operator fun invoke(requestEnvelope: Envelope): Envelope? {
        return requestEnvelope.takeIf { validateSignature(it) }?.let {
            handle(it)

            val responsePayload = buildResponsePayload(it).toByteArray()

            val securityKey = with(it) {
                SigningUtil.createSignature(
                    sequenceNumber.toByteArray(2) +
                            deviceId +
                            lengthIndicator.toByteArray(2) +
                            messageBytes
                )
            }

            Envelope(
                securityKey,
                it.sequenceNumber,
                it.deviceId,
                responsePayload.size,
                responsePayload
            )
        }
    }

    private fun validateSignature(requestEnvelope: Envelope): Boolean {
        val verified = with(requestEnvelope) {
            SigningUtil.verifySignature(
                sequenceNumber.toByteArray(2) +
                        deviceId +
                        lengthIndicator.toByteArray(2) +
                        messageBytes,
                securityKey,
            )
        }

        if (verified) {
            Logger.logReceive("The signature is not valid!")
            return false
        }
        return true
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
