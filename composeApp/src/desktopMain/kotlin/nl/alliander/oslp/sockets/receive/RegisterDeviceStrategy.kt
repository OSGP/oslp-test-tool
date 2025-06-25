package nl.alliander.oslp.sockets.receive

import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.service.DeviceStateService
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.Message
import kotlin.random.Random

class RegisterDeviceStrategy : ReceiveStrategy() {
    override fun matches(message: Message): Boolean = message.hasRegisterDeviceRequest()

    override fun handle(requestEnvelope: Envelope) {
        val deviceStateService = DeviceStateService.getInstance()
        deviceStateService.registerDevice(requestEnvelope.deviceId)
        deviceStateService.randomDevice = requestEnvelope.message.registerDeviceRequest.randomDevice
    }

    override fun buildResponsePayload(requestEnvelope: Envelope): Message {
        val deviceStateService = DeviceStateService.getInstance()

        deviceStateService.deviceId = requestEnvelope.deviceId
        deviceStateService.randomPlatform = Random.nextInt(65536)

        val response = Message.newBuilder()
            .setRegisterDeviceResponse(
                Oslp.RegisterDeviceResponse.newBuilder()
                    .setRandomDevice(requestEnvelope.message.registerDeviceRequest.randomDevice)
                    .setCurrentTime(System.currentTimeMillis().toString())
                    .setStatus(Oslp.Status.OK)
                    .setRandomPlatform(deviceStateService.randomPlatform)
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
