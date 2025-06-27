package nl.alliander.oslp.sockets

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.models.AppConfiguration
import nl.alliander.oslp.service.DeviceStateService
import nl.alliander.oslp.util.Logger

class ClientSocket(
    private val appConfiguration: AppConfiguration
) {

    fun sendAndReceive(envelope: Envelope): Envelope = runBlocking(Dispatchers.IO) {
        val clientSocket: Socket = aSocket(ActorSelectorManager(Dispatchers.IO))
            .tcp()
            .connect(InetSocketAddress(appConfiguration.clientAddress, appConfiguration.clientPort))

        clientSocket.use {
            val output = it.openWriteChannel(autoFlush = true)
            val input = it.openReadChannel()

            val requestEnvelope: ByteArray = envelope.getBytes()

            Logger.logSend(envelope)

            output.writeFully(requestEnvelope, 0, requestEnvelope.size)

            val buffer = ByteArray(1024)
            val bytesRead = input.readAvailable(buffer)

            if (bytesRead > 0) {
                val deviceStateService = DeviceStateService.getInstance()

                val responseEnvelope = Envelope.parseFrom(buffer.copyOf(bytesRead))
                deviceStateService.updateSequenceNumber(responseEnvelope.sequenceNumber)

                Logger.logReceive(responseEnvelope)

                return@runBlocking responseEnvelope
            }
        }
        throw Exception()
    }

}
