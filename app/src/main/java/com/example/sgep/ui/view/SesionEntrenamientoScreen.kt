package com.example.sgep.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.sgep.data.entity.EjercicioEnRutinaEntity
import com.example.sgep.data.entity.EjercicioPredefinidoEntity
import com.example.sgep.viewmodel.RutinaViewModel
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * Pantalla para la sesión de entrenamiento de una rutina específica.
 *
 * @param rutinaId ID de la rutina que se está ejecutando.
 * @param ejerciciosEnRutina Lista de ejercicios asignados a la rutina.
 * @param ejerciciosPredefinidos Lista de ejercicios predefinidos para obtener detalles.
 * @param rutinaViewModel ViewModel que maneja la lógica de la rutina.
 * @param onBack Callback para manejar la acción de volver atrás.
 * @param onFinalizarSesion Callback para manejar la finalización de la sesión.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SesionEntrenamientoScreen(
    rutinaId: Int,
    ejerciciosEnRutina: List<EjercicioEnRutinaEntity>,
    ejerciciosPredefinidos: List<EjercicioPredefinidoEntity>,
    rutinaViewModel: RutinaViewModel,
    onBack: () -> Unit,
    onFinalizarSesion: () -> Unit
) {
    // Estado para guardar el ID de la sesión actual iniciada
    var sesionId by remember { mutableStateOf<Long?>(null) }

    // Mapa mutable que asocia el ID del ejercicioEnRutina con la lista de series en UI
    val seriesPorEjercicio = remember { mutableStateMapOf<Int, SnapshotStateList<SerieUI>>() }

    // Lanzar efecto solo una vez para iniciar la sesión y obtener el sesionId
    LaunchedEffect(Unit) {
        rutinaViewModel.iniciarSesion(rutinaId) { nuevaSesionId ->
            sesionId = nuevaSesionId
        }
    }

    // Calcular el volumen total de la sesión: suma (peso * repeticiones) de todas las series completadas
    val volumenTotal = seriesPorEjercicio.values.flatten()
        .filter { it.done }
        .fold(0f) { acc, serie ->
            val peso = serie.kg.text.toFloatOrNull() ?: 0f
            val reps = serie.reps.text.toIntOrNull() ?: 0
            acc + (peso * reps)
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sesión de entrenamiento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ) {
                    // Mostrar el volumen total calculado
                    Text(
                        "Volumen total: ${"%.0f".format(volumenTotal)} kg",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Botón para finalizar la sesión
                    Button(
                        onClick = onFinalizarSesion,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Finalizar sesión")
                    }
                }
            }
        }
    ) { innerPadding ->
        // Lista perezosa que muestra todos los ejercicios de la rutina
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding(),
                bottom = 100.dp // Espacio para el bottomBar
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(ejerciciosEnRutina) { ejercicioEnRutina ->

                // Buscar los datos del ejercicio predefinido asociado
                val ejercicio = ejerciciosPredefinidos.find { it.id == ejercicioEnRutina.ejercicioPredefinidoId }

                // Obtener o inicializar la lista de series para este ejercicio
                val series = seriesPorEjercicio.getOrPut(ejercicioEnRutina.id) { mutableStateListOf() }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        // Nombre del ejercicio
                        Text(
                            text = ejercicio?.nombre ?: "Ejercicio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Iterar y mostrar cada serie con sus controles
                        series.forEachIndexed { index, serie ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Serie ${index + 1}",
                                    modifier = Modifier.width(70.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                // Campo para ingresar repeticiones
                                OutlinedTextField(
                                    value = serie.reps,
                                    onValueChange = { serie.reps = it },
                                    label = { Text("Reps") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp)
                                )

                                // Campo para ingresar peso en kg
                                OutlinedTextField(
                                    value = serie.kg,
                                    onValueChange = { serie.kg = it },
                                    label = { Text("Kg") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp)
                                )

                                // Checkbox para marcar si la serie está completada
                                Checkbox(
                                    checked = serie.done,
                                    onCheckedChange = {
                                        serie.done = it
                                        // Si la serie se marca como completada y la sesión está iniciada,
                                        // se registra la serie en el ViewModel
                                        if (it && sesionId != null) {
                                            rutinaViewModel.registrarSerieSesion(
                                                sesionRutinaId = sesionId!!.toInt(),
                                                ejercicioEnRutinaId = ejercicioEnRutina.id,
                                                numeroSerie = index + 1,
                                                peso = serie.kg.text.toFloatOrNull() ?: 0f,
                                                repeticiones = serie.reps.text.toIntOrNull() ?: 0,
                                                completada = true
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        // Botón para agregar una nueva serie vacía
                        Button(
                            onClick = { series.add(SerieUI()) },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 8.dp)
                        ) {
                            Text("Agregar serie")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Clase que representa una serie en la UI con campos editables para reps, kg y estado completado.
 */
class SerieUI {
    var reps by mutableStateOf(TextFieldValue(""))
    var kg by mutableStateOf(TextFieldValue(""))
    var done by mutableStateOf(false)
}