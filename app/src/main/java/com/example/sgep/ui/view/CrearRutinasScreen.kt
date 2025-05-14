package com.example.sgep.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sgep.viewmodel.EjercicioViewModel
import com.example.sgep.data.entity.EjercicioEntity
import com.example.sgep.data.entity.RutinaEntity

@Composable
fun CrearRutinaScreen(
    rutinaId: Int? = null,
    onAgregarEjercicio: (EjercicioEntity) -> Unit, // Aunque ya no se usa directamente aquí, la mantendremos por ahora por si es necesaria externamente.
    onGuardarRutina: (RutinaEntity) -> Unit,
    ejercicioViewModel: EjercicioViewModel
) {
    var currentRutina by remember { mutableStateOf(RutinaEntity(ejercicios = mutableListOf())) } // Initialize with a mutable list
    var showDialog by remember { mutableStateOf(false) }
    var nombreRutina by remember { mutableStateOf(currentRutina.nombre) } // Mantenemos el nombre como estado separado para el TextField

    LaunchedEffect(rutinaId) { // Observa el rutinaId para cargar la rutina si existe
        if (rutinaId != null) {
            // Por ahora, simulamos la carga con una rutina de ejemplo
            currentRutina = RutinaEntity(id = rutinaId, nombre = "Rutina Cargada", ejercicios = listOf(
                EjercicioEntity(nombre = "Simulación Ejercicio 1"),
                EjercicioEntity(nombre = "Simulación Ejercicio 2")
            ).toMutableList()) // Asegurarse de que sea MutableList
        }else {
            currentRutina = RutinaEntity() // Crea una nueva rutina vacía si no hay ID
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = nombreRutina,
            onValueChange = { nombreRutina = it }, // Actualiza solo el estado local del nombre
            label = { Text("Nombre de la rutina") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            showDialog = true
        }) {
            Text("Agregar Ejercicio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Ejercicios en la rutina:", style = MaterialTheme.typography.titleMedium)
        // Mostrar la lista de ejercicios en la rutina
        currentRutina.ejercicios.forEach { ejercicio ->
            Text(text = ejercicio.nombre, style = MaterialTheme.typography.bodyLarge) // Muestra el nombre del ejercicio
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onGuardarRutina(currentRutina) }) {
            Text("Guardar Rutina")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Seleccionar Ejercicios") },
            text = { // Esta lambda define el contenido principal del AlertDialog
                LazyColumn { // Aquí comienza el LazyColumn para mostrar la lista
                    items(ejercicioViewModel.listaEjerciciosPredeterminados) { ejercicio -> // Itera sobre la lista de ejercicios
                        Text(
                            text = ejercicio.nombre, // Muestra el nombre del ejercicio
                            modifier = Modifier.clickable { // Aplica el modificador clickable al Text para que sea seleccionable
                                // Añade el ejercicio a la lista mutable de la rutina actual
                                val updatedEjercicios = currentRutina.ejercicios?.toMutableList() ?: mutableListOf()
                                updatedEjercicios.add(ejercicio)
                                currentRutina = currentRutina.copy(ejercicios = updatedEjercicios)
                                showDialog = false // Cierra el diálogo después de seleccionar un ejercicio
                            }
                        )
                    }
                } // Cierra el LazyColumn. ¡Sin coma aquí!
            }, // Cierra la lambda 'text'
            confirmButton = { // Esta lambda define el botón de confirmación
                Button(onClick = { showDialog = false }) { // Aquí comienza el Button
                    Text("Cerrar") // El Text dentro del Button
                } // Cierra el Button
            } // Cierra la lambda 'confirmButton'
        ) // Cierra el AlertDialog
    }
}
