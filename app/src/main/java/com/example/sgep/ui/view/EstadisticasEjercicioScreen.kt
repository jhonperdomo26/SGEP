package com.example.sgep.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.sgep.viewmodel.RutinaViewModel
import kotlin.math.roundToInt
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import androidx.compose.ui.viewinterop.AndroidView
import android.graphics.Color as AndroidColor

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

    val etiquetas = volumenPorSesion.keys.mapIndexed { index, _ -> "Sesión ${index + 1}" }
    val volumenValores = volumenPorSesion.values.toList()

    // Para las otras 3 gráficas, calculamos listas de valores por sesión (usando misma clave sesionRutinaId)
    val mayorPesoPorSesion = series.groupBy { it.sesionRutinaId }.mapValues { entry ->
        entry.value.maxOfOrNull { it.peso } ?: 0f
    }.values.toList()

    val mejor1RMPorSesion = series.groupBy { it.sesionRutinaId }.mapValues { entry ->
        entry.value.maxOfOrNull { it.peso * (1 + it.repeticiones / 30f) } ?: 0f
    }.values.toList()

    val volumenSeriePorSesion = series.groupBy { it.sesionRutinaId }.mapValues { entry ->
        entry.value.maxOfOrNull { it.peso * it.repeticiones } ?: 0f
    }.values.toList()

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
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState), // Habilita scroll vertical
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("📊 Mayor peso levantado: ${"%.1f".format(mayorPeso)} kg")
                    Text("🏋️ Mejor 1RM estimado: $mejor1RM kg")
                    Text("📈 Mejor volumen de serie: ${"%.1f".format(mejorVolumenSerie)} kg")
                    Text("📦 Mejor volumen de sesión: ${"%.1f".format(mejorVolumenSesion)} kg")
                }
            }

            if (volumenValores.isNotEmpty()) {
                // 1. Volumen por sesión
                Text(
                    text = "Volumen por sesión",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                LineChartEstadisticas(
                    valores = volumenValores,
                    etiquetas = etiquetas,
                    titulo = "Volumen por sesión",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                // 2. Mayor peso por sesión
                Text(
                    text = "Mayor peso por sesión",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
                LineChartEstadisticas(
                    valores = mayorPesoPorSesion,
                    etiquetas = etiquetas,
                    titulo = "Mayor peso por sesión",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                // 3. Mejor 1RM estimado por sesión
                Text(
                    text = "Mejor 1RM estimado por sesión",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
                LineChartEstadisticas(
                    valores = mejor1RMPorSesion,
                    etiquetas = etiquetas,
                    titulo = "Mejor 1RM estimado por sesión",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                // 4. Mejor volumen de serie por sesión
                Text(
                    text = "Mejor volumen de serie por sesión",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
                LineChartEstadisticas(
                    valores = volumenSeriePorSesion,
                    etiquetas = etiquetas,
                    titulo = "Mejor volumen de serie por sesión",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            } else {
                Text("No hay datos suficientes para mostrar gráficos.")
            }
        }
    }
}

@Composable
fun LineChartEstadisticas(
    valores: List<Float>,
    etiquetas: List<String>,
    titulo: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                setDrawGridBackground(false)
                setTouchEnabled(true)
                description = Description().apply { text = titulo }
                legend.isEnabled = false
                animateX(1000)
                animateY(1000)
                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val entries = valores.mapIndexed { index, valor ->
                Entry(index.toFloat(), valor)
            }
            val dataSet = LineDataSet(entries, titulo).apply {
                color = AndroidColor.rgb(33, 150, 243)  // azul
                valueTextColor = AndroidColor.WHITE
                valueTextSize = 12f
                setDrawCircles(true)
                circleRadius = 5f
                setCircleColor(AndroidColor.rgb(33, 150, 243))
                lineWidth = 3f
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawValues(true)
            }

            chart.xAxis.apply {
                granularity = 1f
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter(etiquetas)
                position = XAxis.XAxisPosition.BOTTOM
                textColor = AndroidColor.WHITE
                textSize = 12f
            }

            chart.axisLeft.axisMinimum = 0f
            chart.axisLeft.textColor = AndroidColor.WHITE

            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
}

