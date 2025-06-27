package nl.alliander.oslp.sockets

import com.google.protobuf.InvalidProtocolBufferException
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.models.AppConfiguration
import nl.alliander.oslp.sockets.receive.ReceiveStrategy
import nl.alliander.oslp.util.Logger

class ServerSocket {
    @OptIn(DelicateCoroutinesApi::class)
    fun startListening(appConfiguration: AppConfiguration) {
        GlobalScope.launch {
            val serverSocket = aSocket(ActorSelectorManager(Dispatchers.IO))
                .tcp()
                .bind(
                    InetSocketAddress(
                        appConfiguration.connectionConfiguration.serverSocketAddress,
                        appConfiguration.connectionConfiguration.serverSocketPort
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

                        responseStrategy(
                            requestEnvelope,
                            appConfiguration.keys,
                            appConfiguration.locationConfiguration
                        )?.let { envelope ->
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

