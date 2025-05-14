package com.example.sgep.ui.view // Asegúrate de que este paquete sea correcto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn // Importa LazyColumn
import androidx.compose.foundation.lazy.items // Importa items para LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card // Puedes usar Card para cada elemento de la lista
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider // Opcional, para separar elementos
import androidx.compose.material3.ExperimentalMaterial3Api // Si usas clics en Cards
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // Importa getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.viewmodel.EjercicioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(
    navController: NavHostController,
    ejercicioViewModel: EjercicioViewModel
) {
    // *** Observa la lista de rutinas del ViewModel ***
    val rutinas: List<RutinaEntity> by ejercicioViewModel.todasLasRutinas.observeAsState(emptyList()) // Observa el LiveData

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Cambiado a Top para que la lista empiece arriba
    ) {
        Text(text = "Tus Rutinas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Botón para crear nueva rutina (se mantiene arriba)
        Button(onClick = { navController.navigate("crearRutina") }) {
            Text("Crear Nueva Rutina")
        }
        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el botón y la lista

        // *** Mostrar la lista de rutinas ***
        if (rutinas.isEmpty()) {
            Text("Aún no tienes rutinas guardadas.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp) // Opcional, añadir algo de padding
            ) {
                items(rutinas) { rutina ->
                    // Representación simple de cada rutina
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp), // Espacio entre tarjetas
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        onClick = {
                            // TODO: Implementar navegación para ver/editar la rutina
                            // navController.navigate("verRutina/${rutina.id}")
                        }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = rutina.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Fecha: ${rutina.fechaCreacion}", // Muestra la fecha
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            // Aquí podrías añadir un resumen de ejercicios si es relevante
                        }
                    }
                    // Opcional: Un divisor entre elementos si no usas tarjetas con padding
                    // Divider()
                }
            }
        }
    }
}

// *** Eliminada la función PerfilScreen de este archivo ***
