package com.richi_mc.tlaliwareapp.ui.presentation

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.richi_mc.tlaliwareapp.ui.FlowerPootDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    @ApplicationContext
    private val application: Context
) : ViewModel() {
    private val bluetoothAdapter: BluetoothAdapter? = (application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

    private val _devices = MutableStateFlow<List<BluetoothDevice>>( emptyList() )
    val devices: StateFlow<List<BluetoothDevice>> = _devices

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothDevice.ACTION_FOUND) {
                Log.d("FLOWERPOT HOME", "Bluetooth device found")
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    if (!_devices.value.contains(it)) {
                        Log.d("FLOWERPOT HOME", "$it")
                        _devices.value = _devices.value + it
                    }
                }
            }
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun initScan() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        application.registerReceiver(receiver, filter)
        bluetoothAdapter?.startDiscovery()
    }

    override fun onCleared() {
        super.onCleared()
        try {
            application.unregisterReceiver(receiver)
        } catch (_: Exception) {
            Log.d("FLOWERPOT HOME", "Receiver not registered")
        }
    }
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun onSelectDevice( navController: NavController, device: BluetoothDevice ) {

        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("FLOWERPOT HOME", device.name)
            navController.navigate(FlowerPootDevice(device.name, device.address))
        }
    }
}