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
    var sesionId by remember { mutableStateOf<Long?>(null) }

    val seriesPorEjercicio = remember { mutableStateMapOf<Int, SnapshotStateList<SerieUI>>() }

    LaunchedEffect(Unit) {
        rutinaViewModel.iniciarSesion(rutinaId) { nuevaSesionId ->
            sesionId = nuevaSesionId
        }
    }

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
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Volumen total: ${"%.0f".format(volumenTotal)} kg")
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onFinalizarSesion,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar sesión")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(ejerciciosEnRutina) { ejercicioEnRutina ->
                val ejercicio = ejerciciosPredefinidos.find { it.id == ejercicioEnRutina.ejercicioPredefinidoId }
                val series = seriesPorEjercicio.getOrPut(ejercicioEnRutina.id) { mutableStateListOf() }

                Column(Modifier.padding(16.dp)) {
                    Text(text = ejercicio?.nombre ?: "Ejercicio", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    series.forEachIndexed { index, serie ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("Serie ${index + 1}", modifier = Modifier.width(70.dp))

                            OutlinedTextField(
                                value = serie.reps,
                                onValueChange = { serie.reps = it },
                                label = { Text("Reps") },
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                            )

                            OutlinedTextField(
                                value = serie.kg,
                                onValueChange = { serie.kg = it },
                                label = { Text("Kg") },
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                            )
                            Checkbox(
                                checked = serie.done,
                                onCheckedChange = {
                                    serie.done = it
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

                    Button(
                        onClick = {
                            series.add(SerieUI())
                        },
                        modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                    ) {
                        Text("Agregar serie")
                    }
                }
            }
        }
    }
}

class SerieUI {
    var reps by mutableStateOf(TextFieldValue(""))
    var kg by mutableStateOf(TextFieldValue(""))
    var done by mutableStateOf(false)
}