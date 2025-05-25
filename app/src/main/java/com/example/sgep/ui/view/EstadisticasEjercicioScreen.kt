package com.example.sgep.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sgep.viewmodel.RutinaViewModel
import kotlin.math.roundToInt
import androidx.compose.material3.TopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasEjercicioScreen(
    ejercicioEnRutinaId: Int,
    nombreEjercicio: String,
    grupoMuscular: String,  // Nuevo parámetro
    descripcion: String,    // Nuevo parámetro
    rutinaViewModel: RutinaViewModel,
    onBack: () -> Unit
) {
    val series by rutinaViewModel.seriesPorEjercicio.collectAsState()

    LaunchedEffect(ejercicioEnRutinaId) {
        rutinaViewModel.cargarSeriesPorEjercicio(ejercicioEnRutinaId)
    }

    val mayorPeso = series.maxOfOrNull { it.peso } ?: 0f
    val mejor1RM = series.maxOfOrNull {
        it.peso * (1 + it.repeticiones / 30f)
    }?.roundToInt() ?: 0
    val mejorVolumenSerie = series.maxOfOrNull {
        it.peso * it.repeticiones
    } ?: 0f
    val volumenPorSesion = series.groupBy { it.sesionRutinaId }.mapValues { entry ->
        entry.value.fold(0f) { acc, serie -> acc + (serie.peso * serie.repeticiones) }
    }
    val mejorVolumenSesion = volumenPorSesion.values.maxOrNull() ?: 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${nombreEjercicio}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Nueva sección con información del ejercicio
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Grupo muscular: $grupoMuscular",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Descripción: $descripcion",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = descripcion,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Sección de estadísticas (existente)
            Text(
                "Estadísticas:",
                style = MaterialTheme.typography.titleMedium
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Mayor peso levantado: ${"%.1f".format(mayorPeso)} kg")
                    Text("Mejor 1RM estimado: $mejor1RM kg")
                    Text("Mejor volumen de serie: ${"%.1f".format(mejorVolumenSerie)} kg")
                    Text("Mejor volumen de sesión: ${"%.1f".format(mejorVolumenSesion)} kg")
                }
            }
        }
    }
}