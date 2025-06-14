package com.richi_mc.tlaliwareapp.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.richi_mc.tlaliwareapp.R
import com.richi_mc.tlaliwareapp.ui.FlowerPootDevice
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*

@Composable
fun FlowerPootScreen(flowerPotViewModel: FlowerPotViewModel, device: FlowerPootDevice) {
    val message = flowerPotViewModel.message.collectAsState()
    val irrigationTime = flowerPotViewModel.irrigationTime.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var tempTime by remember { mutableStateOf(irrigationTime.value.toString()) }

    LaunchedEffect(irrigationTime.value) {
        tempTime = irrigationTime.value.toString()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Settings, contentDescription = "Configuración")
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) { padding ->
        Column( modifier = Modifier.padding(padding)) {

            Text(
                text = "Conectado a ${device.name}",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Dirección: ${device.address}",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            Icon(
                painter = painterResource(R.drawable.baseline_local_florist_24),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null
            )

            Text(
                text = message.value,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    flowerPotViewModel.sendCommand("1")
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text("Regar ahora")
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Configuración de riego") },
                text = {
                    Column {
                        Text(
                            text = "Tiempo actual: ${irrigationTime.value} segundos",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Nuevo tiempo de riego en segundos:")
                        OutlinedTextField(
                            value = tempTime,
                            onValueChange = { tempTime = it },
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        tempTime.toIntOrNull()?.let {
                            flowerPotViewModel.updateIrrigationTime(it)
                            flowerPotViewModel.sendCommand("" + it)
                        }
                        showDialog = false
                    }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}