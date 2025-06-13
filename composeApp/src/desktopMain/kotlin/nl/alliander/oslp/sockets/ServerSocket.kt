package nl.alliander.oslp.sockets

import com.google.protobuf.InvalidProtocolBufferException
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.network.sockets.port
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.service.LoggingService
import nl.alliander.oslp.sockets.receive.ReceiveStrategy

class ServerSocket(
    private val log: LoggingService,
) {
    @OptIn(DelicateCoroutinesApi::class)
    fun startListening() {
        GlobalScope.launch {
            val serverSocket = aSocket(ActorSelectorManager(Dispatchers.IO))
                .tcp()
                .bind(InetSocketAddress("localhost", 12122))
            log.log("Server is listening on port ${serverSocket.port}")

            while (true) {
                val socket = serverSocket.accept()
                log.log("Accepted connection from ${socket.remoteAddress}")

                val input = socket.openReadChannel()
                val output = socket.openWriteChannel(autoFlush = true)

                try {
                    val buffer = ByteArray(1024)
                    val bytesRead = input.readAvailable(buffer)

                    if (bytesRead > 0) {
                        val requestEnvelope = Envelope.parseFrom(buffer.copyOf(bytesRead))

                        log.logReceive("SequenceNumber: ${requestEnvelope.sequenceNumber}")
                        log.logReceive("LengthIndicator: ${requestEnvelope.lengthIndicator}")
                        log.logReceive("Received payload: ${requestEnvelope.message}")

                        val responseStrategy = ReceiveStrategy.getStrategyFor(requestEnvelope.message)

                        val responseEnvelope: Envelope = responseStrategy(requestEnvelope)

                        delay(1000)

                        val responseBytes = responseEnvelope.getBytes()
                        output.writeFully(responseBytes)
                        log.logSend("Sent response: ${responseEnvelope.message}")
                    }
                } catch (e: InvalidProtocolBufferException) {
                    println("Failed to parse Protobuf message: ${e.message}")
                } catch (e: Exception) {
                    log.log(e.message ?: e.toString())
                } finally {
                    println("Socket closed")
                    socket.close()
                }
            }
        }
    }
}

