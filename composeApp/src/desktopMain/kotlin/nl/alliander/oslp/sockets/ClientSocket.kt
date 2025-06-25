package nl.alliander.oslp.sockets

import androidx.compose.runtime.remember
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.models.PortConfigurationModel
import nl.alliander.oslp.service.DeviceStateService
import nl.alliander.oslp.util.Logger

class ClientSocket(
    private val portConfigurationModel: PortConfigurationModel
) {

    fun sendAndReceive(envelope: Envelope): Envelope = runBlocking(Dispatchers.IO) {
        val clientSocket: Socket = aSocket(ActorSelectorManager(Dispatchers.IO))
            .tcp()
            .connect(InetSocketAddress(portConfigurationModel.clientAddress, portConfigurationModel.clientPort))

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
