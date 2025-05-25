package com.example.sgep.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.data.entity.EjercicioEnRutinaEntity
import com.example.sgep.data.entity.EjercicioPredefinidoEntity
import com.example.sgep.viewmodel.RutinaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRutinaScreen(
    rutina: RutinaEntity,
    rutinaViewModel: RutinaViewModel,
    ejerciciosEnRutina: List<EjercicioEnRutinaEntity>,
    ejerciciosPredefinidos: List<EjercicioPredefinidoEntity>,
    onAgregarEjercicio: (Int) -> Unit,
    onEliminarRutina: () -> Unit,
    onBack: () -> Unit,
    onIniciarSesion: () -> Unit,
    navController: NavController
    ){
    var showAgregarDialog by remember { mutableStateOf(false) }
    var ejercicioSeleccionadoId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(rutina.nombre) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onEliminarRutina) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar rutina")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Información básica de la rutina
            Text(
                text = "Fecha de creación: ${rutina.fechaCreacion}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ejercicios de la rutina:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Lista de ejercicios en la rutina
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (ejerciciosEnRutina.isEmpty()) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay ejercicios en esta rutina.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(ejerciciosEnRutina) { ejercicioEnRutina ->
                            val ejercicio = ejerciciosPredefinidos.find { it.id == ejercicioEnRutina.ejercicioPredefinidoId }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        val encodedNombre = java.net.URLEncoder.encode(ejercicio?.nombre ?: "Ejercicio", "UTF-8")
                                        val encodedGrupo = java.net.URLEncoder.encode(ejercicio?.grupoMuscular ?: "Grupo muscular", "UTF-8")
                                        val encodedDesc = java.net.URLEncoder.encode(ejercicio?.descripcion ?: "Descripción no disponible", "UTF-8")
                                        navController.navigate("estadisticas_ejercicio/${ejercicioEnRutina.id}/$encodedNombre/$encodedGrupo/$encodedDesc")

                                    }
                            ) {
                                Row(
                                    Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        text = ejercicio?.nombre ?: "Ejercicio ID: ${ejercicioEnRutina.ejercicioPredefinidoId}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text (text = ejercicio?.grupoMuscular ?: "",
                                        style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        // Espaciado final extra para no tapar el último ítem con los botones
                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { showAgregarDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Agregar ejercicio")
                }
                Button(
                    onClick = { onIniciarSesion() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Iniciar sesión de entrenamiento")
                }
            }
        }
    }

    // AlertDialog para agregar ejercicio predefinido a la rutina
    if (showAgregarDialog) {
        AlertDialog(
            onDismissRequest = { showAgregarDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        ejercicioSeleccionadoId?.let {
                            onAgregarEjercicio(it)
                            showAgregarDialog = false
                            ejercicioSeleccionadoId = null
                        }
                    },
                    enabled = ejercicioSeleccionadoId != null
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAgregarDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = {
                Text("Agregar ejercicio predefinido")
            },
            text = {
                Box(Modifier.heightIn(max = 300.dp)) { // Limita la altura si hay muchos ejercicios
                    LazyColumn {
                        items(ejerciciosPredefinidos) { ejercicio ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        ejercicioSeleccionadoId = ejercicio.id
                                    }
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = ejercicio.nombre,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}