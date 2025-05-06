package com.example.sgep.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sgep.data.entity.EjercicioEntity
import com.example.sgep.data.entity.RutinaEntity

@Composable
fun CrearRutinaScreen(
    rutina: RutinaEntity,
    onAgregarEjercicio: (EjercicioEntity) -> Unit
) {
    var nombreRutina by remember { mutableStateOf(rutina.nombre) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = nombreRutina,
            onValueChange = { nombreRutina = it },
            label = { Text("Nombre de la rutina") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Navegar a la pantalla de selección de ejercicios
            // Implementar navegación aquí
        }) {
            Text("Agregar Ejercicio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Ejercicios en la rutina:", style = MaterialTheme.typography.titleMedium)
        rutina.ejercicios.forEach { ejercicio ->
            Text(text = ejercicio.nombre, style = MaterialTheme.typography.bodyLarge)
        }
    }
}