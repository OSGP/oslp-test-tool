package nl.alliander.oslp.sockets.receive

import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.service.DeviceStateService
import nl.alliander.oslp.util.Logger
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.Message

class ConfirmRegisterDeviceStrategy : ReceiveStrategy() {
    private val deviceStateService = DeviceStateService.getInstance()

    override fun matches(message: Message): Boolean = message.hasConfirmRegisterDeviceRequest()

    override fun handle(requestEnvelope: Envelope) {

        with(requestEnvelope.message.confirmRegisterDeviceRequest) {
            if (randomDevice != deviceStateService.randomDevice)
                Logger.logReceive("Invalid randomDevice! Expected: ${deviceStateService.randomDevice} - Got: $randomDevice")
            if (randomPlatform != deviceStateService.randomPlatform)
                Logger.logReceive("Invalid randomPlatform! Expected: ${deviceStateService.randomPlatform} - Got: $randomPlatform")
        }

        deviceStateService.confirmRegisterDevice()
    }

    override fun buildResponsePayload(requestEnvelope: Envelope): Message {
        val response = Message.newBuilder().setConfirmRegisterDeviceResponse(
            Oslp.ConfirmRegisterDeviceResponse.newBuilder()
                .setRandomDevice(deviceStateService.randomDevice)
                .setRandomPlatform(deviceStateService.randomPlatform)
                .setSequenceWindow(1)
                .setStatusValue(0)
                .build()
        ).build()
        return response
    }
}
