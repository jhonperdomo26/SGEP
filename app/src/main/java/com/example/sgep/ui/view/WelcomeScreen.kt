package com.example.sgep.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgep.data.entity.UserEntity
import com.example.sgep.viewmodel.LoginViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Composable que muestra una pantalla de bienvenida para el usuario autenticado.
 *
 * @param user Usuario actual que se muestra inicialmente (puede ser null mientras se obtiene del ViewModel).
 * @param viewModel ViewModel que gestiona la sesión e información del usuario.
 * @param onLogout Callback que se ejecuta cuando el usuario decide cerrar sesión.
 * @param modifier Modifier opcional para personalizar el layout externo.
 */
@Composable
fun WelcomeScreen(
    user: UserEntity?,
    viewModel: LoginViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Observa el estado actual del usuario desde el ViewModel, usando el ciclo de vida para evitar fugas
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Fondo con un degradado vertical que va desde el color primario con baja opacidad hasta el fondo
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Encabezado con texto de bienvenida
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "¡Bienvenido a SGEP!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Espaciador que empuja el contenido principal al centro verticalmente
            Spacer(modifier = Modifier.weight(1f))

            // Sección con información del usuario (nombre y objetivo)
            UserInfoSection(user = currentUser ?: user)

            // Otro espaciador para balancear el diseño verticalmente
            Spacer(modifier = Modifier.weight(1f))

            // Pie de pantalla con línea divisoria y botón para cerrar sesión
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                )

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        "Cerrar sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/**
 * Composable privado que muestra la información principal del usuario.
 *
 * Muestra el nombre y el objetivo del usuario dentro de una tarjeta,
 * y un indicador de carga mientras no se dispone de la información.
 *
 * @param user Usuario actual o null si la información está cargando.
 */
@Composable
private fun UserInfoSection(user: UserEntity?) {
    // Nombre y objetivo con valores por defecto si son nulos o vacíos
    val nombre = user?.nombre?.takeIf { it.isNotBlank() } ?: "Usuario"
    val objetivo = user?.objetivo?.takeIf { it.isNotBlank() } ?: "Aún no has definido un objetivo"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Muestra la tarjeta solo si el usuario existe (animación visible/invisible)
        AnimatedVisibility(visible = user != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Nombre del usuario en estilo destacado
                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Texto indicativo para el objetivo
                    Text(
                        text = "Tu objetivo:",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )

                    // Objetivo del usuario con límite de líneas y texto truncado si es largo
                    Text(
                        text = objetivo,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Mientras no hay usuario, mostrar indicador de carga circular centrado
        if (user == null) {
            CircularProgressIndicator(modifier = Modifier.padding(32.dp))
        }
    }
}