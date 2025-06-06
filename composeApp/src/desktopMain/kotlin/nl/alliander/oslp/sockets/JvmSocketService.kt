import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.Message
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.*
import org.opensmartgridplatform.oslp.Envelope.OslpEnvelope
import org.opensmartgridplatform.oslp.Envelope
import org.opensmartgridplatform.oslp.Oslp

import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class JvmSocketService {
    var onMessage: ((String) -> Unit)? = null
    var log: ((String) -> Unit)? = null

    fun startListening() {
        runBlocking {
            val serverSocket = aSocket(ActorSelectorManager(Dispatchers.IO))
                .tcp()
                .bind(InetSocketAddress("localhost", 12121))
            log?.invoke("Server is listening on port 12121")

            while (true) {
                val socket = serverSocket.accept()
                log?.invoke("Accepted connection from ${socket.remoteAddress}")

                val input = socket.openReadChannel()
                val output = socket.openWriteChannel(autoFlush = true)

                try {
                    // Read incoming bytes
                    val buffer = ByteArray(1024)
                    val bytesRead = input.readAvailable(buffer)
                    if (bytesRead > 0) {
                        // Deserialize the received bytes into an OslpEnvelope
                        val requestEnvelope = OslpEnvelope.parseFrom(buffer.copyOf(bytesRead))
                        log?.invoke("Received: ${requestEnvelope}")

                        // Create a response OslpEnvelope
                        val responseEnvelope: OslpEnvelope = getResponse(requestEnvelope)

                        delay(1000)

                        // Serialize the response into bytes and send it back
                        val responseBytes = responseEnvelope.toByteArray()
                        output.writeFully(responseBytes)
                        log?.invoke("Sent response: ${responseEnvelope}")
                    }
                } catch (e: InvalidProtocolBufferException) {
                    println("Failed to parse Protobuf message: ${e.message}")
                } finally {
                    println("Socket closed")
                    socket.close()
                }
            }
        }

    }

    fun getResponse(request: OslpEnvelope): OslpEnvelope {
        val registrationResponse = Oslp.Message.newBuilder()
            .setRegisterDeviceResponse(
                Oslp.RegisterDeviceResponse.newBuilder()
                    .setRandomDevice(request.payload.registerDeviceRequest.randomDevice)
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

        val response = Envelope.OslpEnvelope.newBuilder()
            .setDeviceId(request.deviceId)
            .setLengthIndicator(request.lengthIndicator)
            .setSecurityKey(request.securityKey)
            .setSequenceNumber(request.sequenceNumber)
            .setPayload(registrationResponse)
            .build()

        return response
    }
}

