package nl.alliander.oslp.service

import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.sockets.ClientSocket
import nl.alliander.oslp.util.SigningUtil
import nl.alliander.oslp.util.toByteArray
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.Message

class RequestService(
    loggingService: LoggingService
) {
    private val clientSocket = ClientSocket(loggingService)

    fun getFirmwareVersion() {
        val payload = Message.newBuilder().setGetFirmwareVersionRequest(
            Oslp.GetFirmwareVersionRequest.newBuilder().build()
        ).build()

        val deviceStateService = DeviceStateService.getInstance()

        val sequenceNumber = deviceStateService.incrementSequenceNumber()
        val deviceId = deviceStateService.deviceId
        val lengthIndicator = payload.serializedSize
        val messageBytes = payload.toByteArray()

        val signature = SigningUtil.createSignature(
            sequenceNumber.toByteArray(2) +
                    deviceId +
                    lengthIndicator.toByteArray(2) +
                    messageBytes
        )

        val request = Envelope(
            signature,
            sequenceNumber,
            deviceId,
            lengthIndicator,
            messageBytes,
        )

        val response = clientSocket.sendAndReceive(request)
    }
}
