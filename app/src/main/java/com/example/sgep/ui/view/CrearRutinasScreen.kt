package com.example.sgep.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sgep.viewmodel.RutinaViewModel

/**
 * Pantalla para crear una nueva rutina personalizada.
 * Permite al usuario ingresar un nombre, seleccionar ejercicios predefinidos
 * y crear una rutina que se asocia a su ID.
 *
 * Características:
 * - Campo de texto para nombrar la rutina.
 * - Botón para confirmar la creación de la rutina.
 * - Lista de ejercicios predefinidos disponibles para agregar.
 * - Lista de ejercicios ya agregados a la rutina (desde el ViewModel).
 *
 * Nota: La asociación de ejercicios a la rutina requiere que la rutina haya sido creada previamente.
 *
 * @param userId ID del usuario que está creando la rutina. Se asocia con la nueva rutina creada.
 * @param rutinaViewModel ViewModel encargado de manejar la lógica de negocio relacionada con las rutinas.
 * @param onRutinaCreada Función de callback que se invoca cuando la rutina ha sido creada exitosamente.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRutinaScreen(
    userId: Int,
    rutinaViewModel: RutinaViewModel,
    onRutinaCreada: () -> Unit
) {
    var nombreRutina by remember { mutableStateOf("") }
    val ejerciciosPredefinidos by rutinaViewModel.ejerciciosPredefinidos.collectAsState()
    val ejerciciosEnRutina by rutinaViewModel.ejerciciosEnRutina.collectAsState()

    // Al entrar, carga los ejercicios predefinidos
    LaunchedEffect(Unit) {
        rutinaViewModel.cargarEjerciciosPredefinidos()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = nombreRutina,
            onValueChange = { nombreRutina = it },
            label = { Text("Nombre de la rutina") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para crear la rutina
        Button(
            onClick = {
                if (nombreRutina.isNotBlank()) {
                    rutinaViewModel.crearRutina(nombreRutina, userId)  // <-- Aquí pasas el userId
                    nombreRutina = ""
                    onRutinaCreada()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = nombreRutina.isNotBlank()
        ) {
            Text("Crear Rutina")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Agregar ejercicios predefinidos:", style = MaterialTheme.typography.titleMedium)
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(ejerciciosPredefinidos) { ejercicio ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            // Aquí puedes asociar este ejercicio a la rutina seleccionada
                            // Debes tener la rutinaId ya creada o seleccionada
                            // rutinaViewModel.agregarEjercicioARutina(rutinaId, ejercicio.id)
                        }
                ) {
                    Row(Modifier.padding(16.dp)) {
                        Text(ejercicio.nombre)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Ejercicios agregados a la rutina:", style = MaterialTheme.typography.titleMedium)
        if (ejerciciosEnRutina.isEmpty()) {
            Text("Aún no hay ejercicios agregados.")
        } else {
            LazyColumn {
                items(ejerciciosEnRutina) { ejercicioEnRutina ->
                    Text("Ejercicio ID: ${ejercicioEnRutina.ejercicioPredefinidoId}")
                    // Si quieres el nombre, búscalo en ejerciciosPredefinidos por id
                    // val ejercicio = ejerciciosPredefinidos.find { it.id == ejercicioEnRutina.ejercicioPredefinidoId }
                    // Text(ejercicio?.nombre ?: "ID: ${ejercicioEnRutina.ejercicioPredefinidoId}")
                }
            }
        }
    }
}