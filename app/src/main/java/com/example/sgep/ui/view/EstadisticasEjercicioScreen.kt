package com.example.sgep.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                title = { Text("Estadísticas de $nombreEjercicio") },
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
            Text("Mayor peso levantado: ${"%.1f".format(mayorPeso)} kg")
            Text("Mejor 1RM estimado: $mejor1RM kg")
            Text("Mejor volumen de serie: ${"%.1f".format(mejorVolumenSerie)} kg")
            Text("Mejor volumen de sesión: ${"%.1f".format(mejorVolumenSesion)} kg")
        }
    }
}