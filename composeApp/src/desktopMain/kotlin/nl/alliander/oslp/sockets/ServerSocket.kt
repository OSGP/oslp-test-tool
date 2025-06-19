/*
 * Copyright 2025 Alliander N.V.
 */
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
import nl.alliander.oslp.sockets.receive.ReceiveStrategy
import nl.alliander.oslp.util.Logger

class ServerSocket {
    @OptIn(DelicateCoroutinesApi::class)
    fun startListening() {
        GlobalScope.launch {
            val serverSocket =
                aSocket(ActorSelectorManager(Dispatchers.IO))
                    .tcp()
                    .bind(InetSocketAddress("localhost", 12122))
            Logger.log("Server is listening on port ${serverSocket.port}")

            while (true) {
                val socket = serverSocket.accept()
                Logger.log("Accepted connection from ${socket.remoteAddress}")

                val input = socket.openReadChannel()
                val output = socket.openWriteChannel(autoFlush = true)

                try {
                    val buffer = ByteArray(1024)
                    val bytesRead = input.readAvailable(buffer)

                    if (bytesRead > 0) {
                        val requestEnvelope = Envelope.parseFrom(buffer.copyOf(bytesRead))

                        Logger.logReceive(requestEnvelope)

                        val responseStrategy = ReceiveStrategy.getStrategyFor(requestEnvelope.message)

                        val responseEnvelope: Envelope = responseStrategy(requestEnvelope)

                        delay(1000)

                        val responseBytes = responseEnvelope.getBytes()
                        output.writeFully(responseBytes)

                        Logger.logSend(responseEnvelope)
                    }
                } catch (e: InvalidProtocolBufferException) {
                    println("Failed to parse Protobuf message: ${e.message}")
                } catch (e: Exception) {
                    Logger.log(e.message ?: e.toString())
                } finally {
                    socket.close()
                }
            }
        }
    }
}
