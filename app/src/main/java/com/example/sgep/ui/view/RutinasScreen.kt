package com.example.sgep.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.viewmodel.RutinaViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla principal para mostrar todas las rutinas, crear nuevas
 * y permitir iniciar una sesión de entrenamiento.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(
    userId: Int,
    rutinaViewModel: RutinaViewModel,
    onRutinaSeleccionada: (RutinaEntity) -> Unit,
    onIniciarSesion: (rutinaId: Int) -> Unit
) {
    val rutinas by rutinaViewModel.rutinas.collectAsState()
    val mensaje by rutinaViewModel.mensaje.collectAsState()
    var nombreRutina by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        rutinaViewModel.cargarRutinas()
    }

    // Mostrar Snackbar y limpiar mensaje tras mostrarlo
    LaunchedEffect(mensaje) {
        mensaje?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                rutinaViewModel.limpiarMensaje()
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            Text(
                text = "Mis Rutinas",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Crear nueva rutina", color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.height(28.dp))

            LazyColumn(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(rutinas) { rutina ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRutinaSeleccionada(rutina) }
                            .padding(horizontal = 4.dp),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            Modifier
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = rutina.nombre,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = { onIniciarSesion(rutina.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(
                                    "Iniciar sesión",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            snackbar = { snackbarData ->
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Confirmación",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = snackbarData.visuals.message)
                    }
                }
            }
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        "Nueva Rutina",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                text = {
                    OutlinedTextField(
                        value = nombreRutina,
                        onValueChange = { nombreRutina = it },
                        label = { Text("Nombre de la rutina") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            rutinaViewModel.crearRutina(nombreRutina, userId)
                            nombreRutina = ""
                            showDialog = false
                        },
                        enabled = nombreRutina.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Crear", color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.primary)
                    }
                },
                shape = MaterialTheme.shapes.large,
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}