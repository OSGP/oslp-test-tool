package nl.alliander.oslp.sockets.receive

import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.models.LocationConfiguration
import nl.alliander.oslp.service.DeviceStateService
import nl.alliander.oslp.util.Logger
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.Message

class EventNotificationRequestStrategy : ReceiveStrategy() {
    private val deviceStateService = DeviceStateService.getInstance()

    override fun matches(message: Message): Boolean = message.hasEventNotificationRequest()

    override fun handle(requestEnvelope: Envelope) {
        deviceStateService.updateSequenceNumber(requestEnvelope.sequenceNumber)
        Logger.logReceive("Received event notification request: ${requestEnvelope.message.eventNotificationRequest}")
    }

    override fun buildResponsePayload(
        requestEnvelope: Envelope,
        locationConfiguration: LocationConfiguration
    ): Message {
        return Message.newBuilder()
            .setEventNotificationResponse(
                Oslp.EventNotificationResponse.newBuilder().setStatus(Oslp.Status.OK)
            )
            .build()
    }
}