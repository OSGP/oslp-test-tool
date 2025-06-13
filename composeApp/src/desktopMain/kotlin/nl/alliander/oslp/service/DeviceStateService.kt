package nl.alliander.oslp.service

import nl.alliander.oslp.models.MainViewModel

class DeviceStateService private constructor(
    private val mainViewModel: MainViewModel
) {
    private var deviceRegistered = false
    private var deviceRegistrationConfirmed = false
    private var relayEnabled = mutableListOf(false, false, false)

    var deviceId: ByteArray = byteArrayOf()
    var sequenceNumber: Int = 0

    fun registerDevice(deviceId: ByteArray) {
        this.deviceId = deviceId
        deviceRegistered = true
        syncState()
    }

    fun confirmRegisterDevice() {
        deviceRegistrationConfirmed = true
        syncState()
    }

    fun incrementSequenceNumber(): Int = ++sequenceNumber

    fun reset() {
        deviceId = byteArrayOf()
        deviceRegistrationConfirmed = false
        deviceRegistered = false
        relayEnabled = mutableListOf(false, false, false)
        syncState()
    }

    private fun syncState() {
        mainViewModel.isDeviceRegistered = deviceRegistered
        mainViewModel.isConfirmed = deviceRegistrationConfirmed
    }

    companion object {
        private var instance: DeviceStateService? = null

        fun createInstance(mainViewModel: MainViewModel) {
            instance = DeviceStateService(mainViewModel)
        }

        fun getInstance(): DeviceStateService =
            instance ?: throw IllegalStateException("No DeviceStateService instance found!")
    }
}
