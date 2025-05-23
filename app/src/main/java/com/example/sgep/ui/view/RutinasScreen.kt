package com.example.sgep.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.viewmodel.RutinaViewModel

/**
 * Pantalla principal para mostrar todas las rutinas, crear nuevas
 * y permitir iniciar una sesión de entrenamiento.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(
    rutinaViewModel: RutinaViewModel,
    onRutinaSeleccionada: (RutinaEntity) -> Unit,
    onIniciarSesion: (rutinaId: Int) -> Unit
) {
    val rutinas by rutinaViewModel.rutinas.collectAsState()
    val mensaje by rutinaViewModel.mensaje.collectAsState()
    var nombreRutina by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Cargar rutinas al entrar en la pantalla
    LaunchedEffect(Unit) {
        rutinaViewModel.cargarRutinas()
    }

    // Mostrar snackbar con mensajes de feedback
    if (mensaje != null) {
        Snackbar(
            modifier = Modifier.padding(8.dp),
            action = {
                TextButton(onClick = { rutinaViewModel.limpiarMensaje() }) { Text("OK") }
            }
        ) { Text(mensaje!!) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Mis Rutinas",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para crear nueva rutina
        Button(onClick = { showDialog = true }) {
            Text("Crear nueva rutina")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de rutinas existentes
        LazyColumn(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(rutinas) { rutina ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRutinaSeleccionada(rutina) }
                        .padding(4.dp)
                ) {
                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = rutina.nombre,
                            modifier = Modifier.weight(1f)
                        )
                        Button(onClick = { onIniciarSesion(rutina.id) }) {
                            Text("Iniciar sesión")
                        }
                    }
                }
            }
        }
    }

    // Diálogo para ingresar el nombre de una nueva rutina
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nueva Rutina") },
            text = {
                OutlinedTextField(
                    value = nombreRutina,
                    onValueChange = { nombreRutina = it },
                    label = { Text("Nombre de la rutina") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        rutinaViewModel.crearRutina(nombreRutina)
                        nombreRutina = ""
                        showDialog = false
                    },
                    enabled = nombreRutina.isNotBlank()
                ) {
                    Text("Crear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            }
        )
    }
}
