package com.example.sgep.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.data.entity.EjercicioEnRutinaEntity
import com.example.sgep.data.entity.EjercicioPredefinidoEntity
import com.example.sgep.viewmodel.RutinaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
) {
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
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            val fechaFormateada = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
                .format(Date(rutina.fechaCreacion))
            // Información de la rutina
            Text(
                text = "Fecha de creación: $fechaFormateada",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Ejercicios de la rutina",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Lista de ejercicios
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (ejerciciosEnRutina.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No hay ejercicios en esta rutina.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ejerciciosEnRutina) { ejercicioEnRutina ->
                            val ejercicio = ejerciciosPredefinidos.find { it.id == ejercicioEnRutina.ejercicioPredefinidoId }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val nombre = ejercicio?.nombre ?: "Ejercicio"
                                        val grupoMuscular = ejercicio?.grupoMuscular ?: "Desconocido"
                                        val descripcion = ejercicio?.descripcion ?: "Sin descripción"

                                        val encodedNombre = java.net.URLEncoder.encode(nombre, "UTF-8")
                                        val encodedGrupo = java.net.URLEncoder.encode(grupoMuscular, "UTF-8")
                                        val encodedDescripcion = java.net.URLEncoder.encode(descripcion, "UTF-8")

                                        navController.navigate("estadisticas_ejercicio/${ejercicioEnRutina.id}/$encodedNombre/$encodedGrupo/$encodedDescripcion")
                                    }

                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = ejercicio?.nombre ?: "Ejercicio ID: ${ejercicioEnRutina.ejercicioPredefinidoId}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showAgregarDialog = true },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        "Agregar ejercicio",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Button(
                    onClick = { onIniciarSesion() },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        "Iniciar sesión",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    // AlertDialog para agregar ejercicio
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
                Text(
                    "Agregar ejercicio predefinido",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Box(
                    Modifier
                        .heightIn(max = 300.dp)
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(ejerciciosPredefinidos) { ejercicio ->
                            Surface(
                                tonalElevation = 2.dp,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        ejercicioSeleccionadoId = ejercicio.id
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth()
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
            }
        )
    }
}
