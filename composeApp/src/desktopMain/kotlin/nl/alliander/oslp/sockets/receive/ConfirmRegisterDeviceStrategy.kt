package nl.alliander.oslp.sockets.receive

import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.service.DeviceStateService
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.Message

class ConfirmRegisterDeviceStrategy : ReceiveStrategy() {
    override fun matches(message: Message): Boolean = message.hasConfirmRegisterDeviceRequest()

    override fun handle(requestEnvelope: Envelope) {
        DeviceStateService.getInstance().confirmRegisterDevice()
    }

    override fun buildResponsePayload(requestEnvelope: Envelope): Message {
        val response = Message.newBuilder().setConfirmRegisterDeviceResponse(
            Oslp.ConfirmRegisterDeviceResponse.newBuilder()
                .setRandomDevice(requestEnvelope.message.confirmRegisterDeviceRequest.randomDevice)
                .setRandomPlatform(requestEnvelope.message.confirmRegisterDeviceRequest.randomPlatform)
                .setSequenceWindow(1)
                .setStatusValue(0)
                .build()
        ).build()
        return response
    }
}
