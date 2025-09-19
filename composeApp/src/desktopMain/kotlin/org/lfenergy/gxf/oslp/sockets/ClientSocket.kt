// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.oslp.sockets

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
import org.lfenergy.gxf.oslp.domain.Envelope
import org.lfenergy.gxf.oslp.models.ApplicationConfiguration
import org.lfenergy.gxf.oslp.service.DeviceStateService
import org.lfenergy.gxf.oslp.util.Logger

class ClientSocket() {

    fun sendAndReceive(envelope: Envelope): Envelope? =
        runBlocking(Dispatchers.IO) {
            val config = ApplicationConfiguration.get()
            val deviceStateService = DeviceStateService.getInstance()

            val clientSocket: Socket =
                aSocket(ActorSelectorManager(Dispatchers.IO))
                    .tcp()
                    .connect(InetSocketAddress(config.clientAddress, config.clientPort))

            clientSocket.use {
                val output = it.openWriteChannel(autoFlush = true)
                val input = it.openReadChannel()

                val requestEnvelope: ByteArray = envelope.getBytes()

                Logger.logSend(envelope)

                output.writeFully(requestEnvelope, 0, requestEnvelope.size)

                if (deviceStateService.isCommunicationDisabled()) {
                    Logger.log("Not listening for a response because the communication is disabled")
                    return@runBlocking null
                }

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
