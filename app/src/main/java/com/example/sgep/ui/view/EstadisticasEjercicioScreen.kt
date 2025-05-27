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
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Pantalla que muestra estadísticas detalladas sobre un ejercicio específico dentro de una rutina,
 * incluyendo datos como el mejor 1RM estimado, el mayor peso levantado y el volumen por serie.
 * Además, presenta gráficos por sesión sobre el progreso en 1RM, peso y volumen.
 *
 * @param ejercicioEnRutinaId ID del ejercicio dentro de la rutina (clave foránea para obtener series).
 * @param nombreEjercicio Nombre codificado del ejercicio (será decodificado para mostrar).
 * @param grupoMuscular Grupo muscular al que pertenece el ejercicio (codificado en URL).
 * @param descripcion Descripción del ejercicio (codificada en URL).
 * @param rutinaViewModel ViewModel que contiene la lógica de recuperación de series y datos.
 * @param onBack Callback para volver a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasEjercicioScreen(
    ejercicioEnRutinaId: Int,
    nombreEjercicio: String,
    grupoMuscular: String,
    descripcion: String,
    rutinaViewModel: RutinaViewModel,
    onBack: () -> Unit
) {
    val nombreEjercicioDecoded = URLDecoder.decode(nombreEjercicio, StandardCharsets.UTF_8.toString())
    val grupoMuscularDecoded = URLDecoder.decode(grupoMuscular, StandardCharsets.UTF_8.toString())
    val descripcionDecoded = URLDecoder.decode(descripcion, StandardCharsets.UTF_8.toString())
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

    val etiquetas = volumenPorSesion.keys.mapIndexed { index, _ -> "Sesión ${index + 1}" }
    val volumenValores = volumenPorSesion.values.toList()

    val mayorPesoPorSesion = series.groupBy { it.sesionRutinaId }.mapValues { entry ->
        entry.value.maxOfOrNull { it.peso } ?: 0f
    }.values.toList()

    val mejor1RMPorSesion = series.groupBy { it.sesionRutinaId }.mapValues { entry ->
        entry.value.maxOfOrNull { it.peso * (1 + it.repeticiones / 30f) } ?: 0f
    }.values.toList()

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(nombreEjercicioDecoded) },
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
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Grupo muscular: $grupoMuscularDecoded", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Descripción: $descripcionDecoded", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Text("Estadísticas:", style = MaterialTheme.typography.titleMedium)

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
                    Text("🏋️ Mejor 1RM estimado: $mejor1RM kg")
                    Text("📊 Mayor peso levantado: ${"%.1f".format(mayorPeso)} kg")
                    Text("📈 Mejor volumen de serie: ${"%.1f".format(mejorVolumenSerie)} kg")
                }
            }

            if (volumenValores.isNotEmpty()) {
                Text("Mejor 1RM estimado por sesión", style = MaterialTheme.typography.titleMedium, color = Color.White)
                LineChartEstadisticas(
                    valores = mejor1RMPorSesion,
                    etiquetas = etiquetas,
                    titulo = "Mejor 1RM estimado por sesión",
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                )

                Text("Mayor peso por sesión", style = MaterialTheme.typography.titleMedium, color = Color.White)
                LineChartEstadisticas(
                    valores = mayorPesoPorSesion,
                    etiquetas = etiquetas,
                    titulo = "Mayor peso por sesión",
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                )

                Text("Mejor volumen de serie por sesión", style = MaterialTheme.typography.titleMedium, color = Color.White)
                LineChartEstadisticas(
                    valores = volumenValores,
                    etiquetas = etiquetas,
                    titulo = "Volumen por sesión",
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                )
            } else {
                Text("No hay datos suficientes para mostrar gráficos.")
            }
        }
    }
}

/**
 * Componente que integra un gráfico de líneas usando MPAndroidChart para mostrar valores
 * estadísticos con etiquetas en el eje X.
 *
 * @param valores Lista de valores flotantes a graficar.
 * @param etiquetas Lista de etiquetas para el eje X (por ejemplo, nombres de sesiones).
 * @param titulo Título que se mostrará como descripción del gráfico.
 * @param modifier Modificador para ajustar el tamaño y disposición del gráfico.
 */
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
                color = AndroidColor.rgb(33, 150, 243)
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