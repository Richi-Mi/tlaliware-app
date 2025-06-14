package com.richi_mc.tlaliwareapp.ui.presentation

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richi_mc.tlaliwareapp.ui.FlowerPootDevice
import com.richi_mc.tlaliwareapp.ui.data.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Scanner
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FlowerPotViewModel @Inject constructor(
    @ApplicationContext
    private val application: Context,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val bluetoothAdapter: BluetoothAdapter? = (application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

    private var outputStream: java.io.OutputStream? = null

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _irrigationTime = MutableStateFlow(5) // tiempo en segundos
    val irrigationTime: StateFlow<Int> = _irrigationTime

    init {
        viewModelScope.launch {
            preferenceRepository.irrigationTime.collect{
                _irrigationTime.value = it
            }
        }
    }

    fun updateIrrigationTime(seconds: Int) {
        viewModelScope.launch {
            preferenceRepository.saveIrrigationTime(seconds)
        }
    }

    suspend fun getSavedMacAddress(): String? {
        return preferenceRepository.macAddress.map { it }.firstOrNull()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connectToFlowerPot(device: FlowerPootDevice) {
        viewModelScope.launch {
            preferenceRepository.saveMacAddress(device.address)
        }
        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModelScope.launch( Dispatchers.IO ) {
                bluetoothAdapter?.cancelDiscovery()

                val bDevice = bluetoothAdapter!!.getRemoteDevice(device.address) // MAC de la ESP32
                val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb") // UUID SPP estándar
                val socket = bDevice.createRfcommSocketToServiceRecord(uuid)

                try {
                    socket.connect()

                    val inputStream = socket.inputStream
                    outputStream = socket.outputStream

                    val scanner = Scanner(inputStream)
                    while (scanner.hasNextLine()) {
                        val message = scanner.nextLine()
                        _message.value = message
                    }
                } catch ( ex: IOException ) {
                    Log.e("Bluetooth", "Error al conectar con la ESP32", ex)
                }
            }
        }
    }
    fun sendCommand(command: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                outputStream?.write((command + "\n").toByteArray())
            } catch (e: IOException) {
                Log.e("Bluetooth", "Error al enviar comando", e)
            }
        }
    }
}

