/*
 * Copyright 2025 Alliander N.V.
 */
package nl.alliander.oslp.service

import com.google.protobuf.kotlin.toByteString
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.alliander.oslp.domain.Envelope
import nl.alliander.oslp.sockets.ClientSocket
import nl.alliander.oslp.util.SigningUtil
import nl.alliander.oslp.util.toByteArray
import org.opensmartgridplatform.oslp.Oslp
import org.opensmartgridplatform.oslp.Oslp.LightValue
import org.opensmartgridplatform.oslp.Oslp.Message

class RequestService() {
    private val clientSocket = ClientSocket()

    fun getFirmwareVersion() {
        val payload =
            Message.newBuilder()
                .setGetFirmwareVersionRequest(Oslp.GetFirmwareVersionRequest.newBuilder().build())
                .build()

        sendAndReceiveRequest(payload)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun startSelfTest() {
        val payload =
            Message.newBuilder().setStartSelfTestRequest(Oslp.StartSelfTestRequest.newBuilder().build()).build()

        sendAndReceiveRequest(payload)

        GlobalScope.launch {
            delay(10_000)
            stopSelfTest()
        }
    }

    private fun stopSelfTest() {
        val payload = Message.newBuilder().setStopSelfTestRequest(Oslp.StopSelfTestRequest.newBuilder().build()).build()

        sendAndReceiveRequest(payload)
    }

    fun startReboot() {
        val deviceStateService = DeviceStateService.getInstance()
        deviceStateService.resetRegistrationValues()

        val payload = Message.newBuilder().setSetRebootRequest(Oslp.SetRebootRequest.newBuilder().build()).build()

        sendAndReceiveRequest(payload)
    }

    fun setLightSensor(num: Int) {
        val payload =
            Message.newBuilder()
                .setSetTransitionRequest(Oslp.SetTransitionRequest.newBuilder().setTransitionTypeValue(num))
                .build()

        sendAndReceiveRequest(payload)
    }

    fun getStatus() {
        val payload = Message.newBuilder().setGetStatusRequest(Oslp.GetStatusRequest.newBuilder().build()).build()

        sendAndReceiveRequest(payload)
    }

    fun getConfiguration() {
        val payload =
            Message.newBuilder().setGetConfigurationRequest(Oslp.GetConfigurationRequest.newBuilder().build()).build()

        sendAndReceiveRequest(payload)
    }

    fun setLightRequest(index: Int, on: Boolean) {
        val lightValue = LightValue.newBuilder()
        lightValue.setOn(on)
        lightValue.setIndex(index.toByteArray(1).toByteString())

        val payload =
            Message.newBuilder()
                .setSetLightRequest(Oslp.SetLightRequest.newBuilder().addValues(lightValue).build())
                .build()

        sendAndReceiveRequest(payload)
    }

    private fun sendAndReceiveRequest(payload: Message) {
        val deviceStateService = DeviceStateService.getInstance()

        val sequenceNumber = deviceStateService.sequenceNumber
        val deviceId = deviceStateService.deviceId
        val lengthIndicator = payload.serializedSize
        val messageBytes = payload.toByteArray()

        val signature =
            SigningUtil.createSignature(
                sequenceNumber.toByteArray(2) + deviceId + lengthIndicator.toByteArray(2) + messageBytes
            )

        val request = Envelope(signature, sequenceNumber, deviceId, lengthIndicator, messageBytes)

        clientSocket.sendAndReceive(request)
    }
}
