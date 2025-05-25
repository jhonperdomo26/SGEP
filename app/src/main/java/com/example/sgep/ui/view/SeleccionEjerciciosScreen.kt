package com.example.sgep.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sgep.data.entity.EjercicioEntity
import com.example.sgep.viewmodel.EjercicioViewModel

@Composable
fun SeleccionEjerciciosScreen(
    viewModel: EjercicioViewModel = viewModel(),
    onEjercicioSeleccionado: (EjercicioEntity) -> Unit
) {
    val ejercicios by viewModel.ejercicios.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarEjercicios()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Selecciona un ejercicio", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        ejercicios.forEach { ejercicio ->
            Text(
                text = ejercicio.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEjercicioSeleccionado(ejercicio) },
                style = MaterialTheme.typography.bodyLarge
            )
            Divider()
        }
    }
}