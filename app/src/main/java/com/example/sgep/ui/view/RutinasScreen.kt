package com.example.sgep.ui.view

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.viewmodel.RutinaViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla principal que muestra todas las rutinas de un usuario.
 * Permite crear nuevas rutinas y comenzar una sesión de entrenamiento
 * basada en una rutina seleccionada.
 *
 * @param userId Identificador del usuario actual, para cargar sus rutinas.
 * @param rutinaViewModel ViewModel que maneja la lógica y datos relacionados con las rutinas.
 * @param onRutinaSeleccionada Callback que se ejecuta cuando el usuario selecciona una rutina
 *                            para editar o ver detalles.
 * @param onIniciarSesion Callback que se ejecuta cuando el usuario decide iniciar una sesión
 *                        de entrenamiento con la rutina seleccionada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(
    userId: Int,
    rutinaViewModel: RutinaViewModel,
    onRutinaSeleccionada: (RutinaEntity) -> Unit,
    onIniciarSesion: (rutinaId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado que contiene la lista actualizada de rutinas del usuario
    val rutinas by rutinaViewModel.rutinas.collectAsState()

    // Estado que contiene el mensaje para mostrar en Snackbar
    val mensaje by rutinaViewModel.mensaje.collectAsState()

    // Estado local para controlar el nombre de la nueva rutina a crear
    var nombreRutina by remember { mutableStateOf("") }

    // Estado para controlar si se debe mostrar el diálogo para crear rutina
    var showDialog by remember { mutableStateOf(false) }

    // Estado para manejar el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // CoroutineScope para lanzar efectos secundarios como mostrar Snackbar
    val scope = rememberCoroutineScope()

    // Cargar rutinas al iniciar la composición, con userId para filtrar
    LaunchedEffect(Unit) {
        rutinaViewModel.cargarRutinas(userId)
    }

    // Mostrar Snackbar cuando cambia el mensaje y luego limpiar el mensaje en ViewModel
    LaunchedEffect(mensaje) {
        mensaje?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                rutinaViewModel.limpiarMensaje()
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        // Fondo degradado de abajo hacia arriba
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 0f,
                        endY = 1000f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Título principal
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Mis Rutinas",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón para mostrar diálogo de creación de rutina nueva
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

            // Lista de rutinas del usuario con LazyColumn
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
                            // Nombre de la rutina
                            Text(
                                text = rutina.nombre,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(12.dp))

                            // Botón para iniciar sesión con esta rutina
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

        // Snackbar para mostrar mensajes de confirmación o error
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

        // Diálogo para crear una nueva rutina
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