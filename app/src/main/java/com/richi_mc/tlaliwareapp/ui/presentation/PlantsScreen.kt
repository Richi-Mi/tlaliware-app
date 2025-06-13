package com.richi_mc.tlaliwareapp.ui.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.richi_mc.tlaliwareapp.R
import com.richi_mc.tlaliwareapp.ui.theme.TlaliwareAppTheme

// Clase base
data class Planta(
    val nombre: String,
    val tipo: String,
    val humedadMin: Int, // porcentaje
    val humedadMax: Int, // porcentaje
    val luz: String,
    val riegoFrecuencia: Int, // en días
    val comentarios: String = ""
)

// Listas organizadas por categoría
val plantasInterior = listOf(
    Planta(
        nombre = "Poto",
        tipo = "Interior",
        humedadMin = 40,
        humedadMax = 70,
        luz = "Media",
        riegoFrecuencia = 5,
        comentarios = "Tolera descuidos. Ideal para principiantes."
    ),
    Planta(
        nombre = "Sansevieria",
        tipo = "Interior",
        humedadMin = 20,
        humedadMax = 40,
        luz = "Baja a media",
        riegoFrecuencia = 10,
        comentarios = "No encharcar. Requiere poco riego."
    ),
    Planta(
        nombre = "Zamioculca",
        tipo = "Interior",
        humedadMin = 30,
        humedadMax = 50,
        luz = "Baja",
        riegoFrecuencia = 7,
        comentarios = "Muy resistente. Crece bien en sombra."
    )
)

val plantasAromaticas = listOf(
    Planta(
        nombre = "Albahaca",
        tipo = "Aromática",
        humedadMin = 50,
        humedadMax = 80,
        luz = "Alta",
        riegoFrecuencia = 2,
        comentarios = "Le gusta el riego constante y el sol."
    ),
    Planta(
        nombre = "Cilantro",
        tipo = "Aromática",
        humedadMin = 40,
        humedadMax = 70,
        luz = "Media a alta",
        riegoFrecuencia = 3,
        comentarios = "Sensible al calor fuerte. Buena ventilación."
    ),
    Planta(
        nombre = "Menta",
        tipo = "Aromática",
        humedadMin = 60,
        humedadMax = 90,
        luz = "Media",
        riegoFrecuencia = 2,
        comentarios = "Crecimiento rápido. Evitar que se seque."
    )
)

val plantasDecorativas = listOf(
    Planta(
        nombre = "Geranio",
        tipo = "Decorativa",
        humedadMin = 40,
        humedadMax = 70,
        luz = "Alta",
        riegoFrecuencia = 3,
        comentarios = "Buena floración si recibe mucho sol."
    ),
    Planta(
        nombre = "Petunia",
        tipo = "Decorativa",
        humedadMin = 50,
        humedadMax = 80,
        luz = "Alta",
        riegoFrecuencia = 2,
        comentarios = "Florece más si se mantiene húmeda."
    ),
    Planta(
        nombre = "Begonia",
        tipo = "Decorativa",
        humedadMin = 40,
        humedadMax = 70,
        luz = "Media",
        riegoFrecuencia = 4,
        comentarios = "Prefiere sombra parcial y humedad estable."
    )
)
@Composable
fun PlantaCard(planta: Planta) {
    val context = LocalContext.current
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                Toast.makeText( context, "Planta seleccionada", Toast.LENGTH_SHORT ).show()
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_local_florist_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(80.dp)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = planta.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "${planta.humedadMin}% < Humedad < ${planta.humedadMax}%")
                Text(text = "Comentarios: ${planta.comentarios}")
            }
        }
    }
}



@Composable
fun PlantsScreen() {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.background( MaterialTheme.colorScheme.background )
    ) { padding ->
        LazyColumn (
            modifier = Modifier.padding(padding)
        ) {
            item {
                Text(
                    text = "Seleccione el tipo de planta que tiene: ",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                HorizontalDivider()
                Text(
                    text = "Interior",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleSmall
                )
            }
            items(plantasInterior.size) { index ->
                PlantaCard(plantasInterior[index])
                Spacer(modifier = Modifier.height(8.dp))

            }
            item {
                Text(
                    text = "Aromáticas",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleSmall
                    )
            }
            items(plantasAromaticas.size) { index ->
                PlantaCard(plantasAromaticas[index])
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Text(
                    text = "Decorativas",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleSmall,
                    )
            }
            items(plantasDecorativas.size) { index ->
                PlantaCard(plantasDecorativas[index])
                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    }
}

@Preview
@Composable
fun PlantsScreenPreview() {
    TlaliwareAppTheme {
        PlantsScreen()
    }
}