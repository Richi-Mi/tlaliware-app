package com.richi_mc.tlaliwareapp.ui.presentation

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.richi_mc.tlaliwareapp.R

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import kotlin.math.*
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.richi_mc.tlaliwareapp.ui.Plants

@Composable
fun DialogEscaneoBluetooth(
    mainViewModel: MainViewModel,
    onDismiss: () -> Unit,
    onSearchClick: (BluetoothDevice) -> Unit
) {
    val devices by mainViewModel.devices.collectAsState()
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        LazyColumn(
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Puntos girando
                    PuntosAlrededor()

                    // Lupa centrada
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscando...",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            item {
                Spacer( Modifier.height(8.dp))
                Text(
                    text = "Buscando...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer( Modifier.height(8.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer( Modifier.height(8.dp))

            }
            items(devices.size) { i ->
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(0.8f)
                            .clickable {
                                onSearchClick(devices[i])
                                onDismiss()
                            }
                    ) {
                        Text(
                            text = devices[i].name ?: "Desconocido",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = devices[i].address ?: "Desconocido",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun PuntosAlrededor() {
    val numPuntos = 6
    val infiniteTransition = rememberInfiniteTransition()
    val angleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing)
        )
    )

    val radius = 100f

    Box(modifier = Modifier.fillMaxSize()) {
        for (i in 0 until numPuntos) {
            val angleDeg = angleOffset + (360f / numPuntos) * i
            val rad = Math.toRadians(angleDeg.toDouble())
            val x = radius * cos(rad).toFloat()
            val y = radius * sin(rad).toFloat()

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .graphicsLayer {
                        translationX = x
                        translationY = y
                    }
                    .align(Alignment.Center)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
            )
        }
    }
}


@Composable
fun MainContent(
    onConfigClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( MaterialTheme.colorScheme.background ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.tlaliware_icon),
            contentDescription = "Tlaliware Icon",
            modifier = Modifier
                .fillMaxWidth( 0.5f )
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Bienvenido a Tlaliware",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Esta app te ayudara a conectar con tus macetas y ver el estado de tus plantas.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(0.5f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = { onSearchClick() }
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Buscar macetas",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer( Modifier.height(16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = { onConfigClick() }
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = null
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Configurar maceta.",
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}


@RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    navController: NavController
) {
    var dialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    mainViewModel.initScan()

    Box {
        if (dialog) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                DialogEscaneoBluetooth(
                    mainViewModel,
                    onDismiss = {
                        dialog = false
                    },
                    onSearchClick = {
                        dialog = false
                        mainViewModel.onSelectDevice( navController, it )
                    }
                )
            }
        }
        MainContent(
            onConfigClick = {
                navController.navigate(Plants)
            }
        ) {
            dialog = true
        }
    }
}