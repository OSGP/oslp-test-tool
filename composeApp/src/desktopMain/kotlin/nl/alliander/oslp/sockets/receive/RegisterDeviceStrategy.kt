package nl.alliander.oslp.sockets.receive

import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.service.DeviceStateService
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.Message

class RegisterDeviceStrategy : ReceiveStrategy() {
    override fun matches(message: Message): Boolean = message.hasRegisterDeviceRequest()

    override fun handle(requestEnvelope: Envelope) {
        DeviceStateService.getInstance().registerDevice(requestEnvelope.deviceId)
    }

    override fun buildResponsePayload(requestEnvelope: Envelope): Message {
        val deviceStateService = DeviceStateService.getInstance()

        deviceStateService.deviceId = requestEnvelope.deviceId
        deviceStateService.sequenceNumber = requestEnvelope.sequenceNumber

        val response = Message.newBuilder()
            .setRegisterDeviceResponse(
                Oslp.RegisterDeviceResponse.newBuilder()
                    .setRandomDevice(requestEnvelope.message.registerDeviceRequest.randomDevice)
                    .setCurrentTime(System.currentTimeMillis().toString())
                    .setStatus(Oslp.Status.OK)
                    .setRandomPlatform(21696)
                    .setLocationInfo(
                        Oslp.LocationInfo.newBuilder()
                            .setLatitude(52132632)
                            .setLongitude(5291266)
                            .setTimeOffset(60)
                    )
                    .build()
            ).build()

        return response
    }

}
