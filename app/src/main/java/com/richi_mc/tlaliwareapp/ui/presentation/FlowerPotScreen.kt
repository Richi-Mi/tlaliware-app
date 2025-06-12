package com.richi_mc.tlaliwareapp.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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

@Composable
fun FlowerPootScreen(flowerPotViewModel: FlowerPotViewModel, device: FlowerPootDevice) {
    val message = flowerPotViewModel.message.collectAsState()
    Scaffold(
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
        }
    }
}