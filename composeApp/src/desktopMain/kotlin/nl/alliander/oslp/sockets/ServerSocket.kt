// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package nl.alliander.oslp.sockets

import com.google.protobuf.InvalidProtocolBufferException
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.models.ConnectionConfiguration
import nl.alliander.oslp.sockets.receive.ReceiveStrategy
import nl.alliander.oslp.util.Logger

class ServerSocket {
    @OptIn(DelicateCoroutinesApi::class)
    fun startListening() {
        GlobalScope.launch {
            val serverSocket =
                aSocket(ActorSelectorManager(Dispatchers.IO))
                    .tcp()
                    .bind(
                        InetSocketAddress(
                            ConnectionConfiguration.serverSocketAddress,
                            ConnectionConfiguration.serverSocketPort,
                        )
                    )
            Logger.log("Server is listening on address: ${serverSocket.localAddress}")

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

                        responseStrategy(requestEnvelope)?.let { envelope ->
                            val responseBytes = envelope.getBytes()
                            output.writeFully(responseBytes)

                            Logger.logSend(envelope)
                        }
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
