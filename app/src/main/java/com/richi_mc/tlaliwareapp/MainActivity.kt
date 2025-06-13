package com.richi_mc.tlaliwareapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.richi_mc.tlaliwareapp.ui.presentation.MainScreen
import com.richi_mc.tlaliwareapp.ui.theme.TlaliwareAppTheme
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.richi_mc.tlaliwareapp.ui.FlowerPootDevice
import com.richi_mc.tlaliwareapp.ui.Main
import com.richi_mc.tlaliwareapp.ui.Plants
import com.richi_mc.tlaliwareapp.ui.presentation.FlowerPootScreen
import com.richi_mc.tlaliwareapp.ui.presentation.FlowerPotViewModel
import com.richi_mc.tlaliwareapp.ui.presentation.MainViewModel
import com.richi_mc.tlaliwareapp.ui.presentation.PlantsScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // TODO: Pedir permisos al usuario y guardarlos en las preferencias.

    private val requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        // Handle the permission request result here
        val allPermissionsGranted = permissions.entries.all { it.value }
        if (allPermissionsGranted) {
                // Permissions granted, you can now use Bluetooth features
                // For example, start scanning or connecting
        } else {
                // Permissions denied, handle this case (e.g., show a message to the user)
        }
    }

    private fun requestBluetoothPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // Permissions for Android 12 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }

        // Location permissions (still needed for some cases or older Android versions)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            // All required permissions are already granted
            // You can proceed with your Bluetooth operations
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestBluetoothPermissions()

        enableEdgeToEdge()
        setContent {
            TlaliwareAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Main
                ) {
                    composable<Plants> {
                        PlantsScreen()
                    }
                    composable<Main> {
                        val mainViewModel = hiltViewModel<MainViewModel>()
                        MainScreen( mainViewModel, navController )
                    }
                    composable<FlowerPootDevice> { backStackEntry ->
                        val device = backStackEntry.toRoute<FlowerPootDevice>()
                        val flowerPotViewModel = hiltViewModel<FlowerPotViewModel>()
                        if (ActivityCompat.checkSelfPermission(
                                application,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            flowerPotViewModel.connectToFlowerPot(device)
                            FlowerPootScreen( flowerPotViewModel, device )
                        }
                    }
                }
            }
        }
    }
}
