package com.example.sgep.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.sgep.data.entity.UserEntity
import com.example.sgep.viewmodel.LoginViewModel
import com.example.sgep.viewmodel.MedidaCorporalViewModel
import com.example.sgep.viewmodel.RutinaViewModel

sealed class Screen(val route: String, val label: String, val icon: @Composable (() -> Unit)) {
    object Inicio : Screen("inicio", "Inicio", { Icon(Icons.Default.Home, contentDescription = "Inicio") })
    object Rutinas : Screen("rutinas", "Rutinas", { Icon(Icons.Default.FitnessCenter, contentDescription = "Rutinas") })
    object Perfil : Screen("perfil", "Perfil", { Icon(Icons.Default.Person, contentDescription = "Perfil") })
    object Medidas : Screen("medidas", "Medidas", { Icon(Icons.Default.Person, contentDescription = "Medidas") })
}

/**
 * Composable principal que representa la pantalla principal de la aplicación.
 *
 * Muestra una Scaffold con una barra de navegación inferior y un host de navegación para gestionar
 * las diferentes pantallas de la app.
 *
 * @param userId Identificador del usuario actualmente autenticado.
 * @param user Entidad del usuario con sus datos (puede ser nulo si no está cargado).
 * @param loginViewModel ViewModel para la gestión de login y sesión.
 * @param onLogout Callback que se ejecuta cuando el usuario cierra sesión.
 * @param rutinaViewModel ViewModel para la gestión de rutinas.
 * @param medidaCorporalViewModel ViewModel para la gestión de medidas corporales.
 */
@Composable
fun MainScreen(
    userId: Int,
    user: UserEntity?,
    loginViewModel: LoginViewModel,
    onLogout: () -> Unit,
    rutinaViewModel: RutinaViewModel,
    medidaCorporalViewModel: MedidaCorporalViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            loginViewModel = loginViewModel,
            user = user,
            userId = userId,
            onLogout = onLogout,
            rutinaViewModel = rutinaViewModel,
            medidaCorporalViewModel = medidaCorporalViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Barra de navegación inferior que permite al usuario cambiar entre las pantallas principales.
 *
 * @param navController Controlador de navegación que maneja las rutas entre pantallas.
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(Screen.Inicio, Screen.Rutinas, Screen.Perfil)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { screen.icon() },
                label = { Text(screen.label) },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * Composable que gestiona la navegación entre las pantallas principales de la aplicación.
 *
 * @param navController Controlador de navegación que administra la navegación entre destinos.
 * @param loginViewModel ViewModel encargado del manejo de la autenticación y login.
 * @param user Entidad del usuario actual (puede ser nulo si no hay usuario logueado).
 * @param userId Identificador único del usuario actual.
 * @param onLogout Callback que se ejecuta al cerrar sesión.
 * @param rutinaViewModel ViewModel encargado de la lógica relacionada con las rutinas de entrenamiento.
 * @param medidaCorporalViewModel ViewModel encargado de la lógica relacionada con las medidas corporales del usuario.
 * @param modifier Modificador opcional para personalizar el layout.
 */
@Composable
fun NavigationHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    user: UserEntity?,
    userId: Int, // ✅ Cambio 4
    onLogout: () -> Unit,
    rutinaViewModel: RutinaViewModel,
    medidaCorporalViewModel: MedidaCorporalViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = Screen.Inicio.route, modifier = modifier) {
        composable(Screen.Inicio.route) {
            WelcomeScreen(
                user = null, // Ya no tienes el UserEntity completo
                viewModel = loginViewModel,
                onLogout = onLogout
            )
        }
        composable(Screen.Rutinas.route) {
            RutinasScreen(
                rutinaViewModel = rutinaViewModel,
                userId = userId,
                onRutinaSeleccionada = { rutina ->
                    navController.navigate("detalle_rutina/${rutina.id}")
                },
                onIniciarSesion = { rutinaId ->
                    navController.navigate("sesion_entrenamiento/$rutinaId")
                }
            )
        }
        composable("estadisticas_ejercicio/{ejercicioEnRutinaId}/{nombreEjercicio}/{grupoMuscular}/{descripcion}") { backStackEntry ->
            val ejercicioEnRutinaId = backStackEntry.arguments?.getString("ejercicioEnRutinaId")?.toIntOrNull() ?: return@composable
            val nombreEjercicio = backStackEntry.arguments?.getString("nombreEjercicio") ?: "Ejercicio"
            val grupoMuscular = backStackEntry.arguments?.getString("grupoMuscular") ?: "Grupo muscular"
            val descripcion = backStackEntry.arguments?.getString("descripcion") ?: "Descripción no disponible"

            EstadisticasEjercicioScreen(
                ejercicioEnRutinaId = ejercicioEnRutinaId,
                nombreEjercicio = nombreEjercicio,
                grupoMuscular = grupoMuscular,
                descripcion = descripcion,
                rutinaViewModel = rutinaViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(
                onMedidasClick = { navController.navigate(Screen.Medidas.route) }
            )
        }
        composable(Screen.Medidas.route) {
            Log.d("MainScreen", "Abriendo MedidaCorporalScreen con userId=$userId") // ✅ Cambio 6
            MedidaCorporalScreen(
                viewModel = medidaCorporalViewModel,
                userId = userId, // ✅ Cambio 5
                onBack = { navController.popBackStack() }
            )
        }
        composable("detalle_rutina/{rutinaId}") { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getString("rutinaId")?.toIntOrNull() ?: return@composable
            val rutina = rutinaViewModel.rutinas.collectAsState().value.find { it.id == rutinaId }
            val ejerciciosEnRutina by rutinaViewModel.ejerciciosEnRutina.collectAsState()
            val ejerciciosPredefinidos by rutinaViewModel.ejerciciosPredefinidos.collectAsState()

            androidx.compose.runtime.LaunchedEffect(rutinaId) {
                rutinaViewModel.cargarEjerciciosEnRutina(rutinaId)
                rutinaViewModel.cargarEjerciciosPredefinidos()
            }

            rutina?.let {
                DetalleRutinaScreen(
                    rutina = it,
                    rutinaViewModel = rutinaViewModel,
                    ejerciciosEnRutina = ejerciciosEnRutina,
                    ejerciciosPredefinidos = ejerciciosPredefinidos,
                    onAgregarEjercicio = { ejercicioId ->
                        rutinaViewModel.agregarEjercicioARutina(it.id, ejercicioId)
                    },
                    onEliminarRutina = { userId ->
                        rutinaViewModel.eliminarRutina(it, userId) {
                            navController.popBackStack()
                        }
                    },
                    onBack = { navController.popBackStack() },
                    onIniciarSesion = {
                        navController.navigate("sesion_entrenamiento/${it.id}")
                    },
                    navController = navController
                )
            }
        }
        composable("sesion_entrenamiento/{rutinaId}") { backStackEntry ->
            val rutinaId = backStackEntry.arguments?.getString("rutinaId")?.toIntOrNull() ?: return@composable
            val ejerciciosEnRutina by rutinaViewModel.ejerciciosEnRutina.collectAsState()
            val ejerciciosPredefinidos by rutinaViewModel.ejerciciosPredefinidos.collectAsState()

            androidx.compose.runtime.LaunchedEffect(rutinaId) {
                rutinaViewModel.cargarEjerciciosEnRutina(rutinaId)
                rutinaViewModel.cargarEjerciciosPredefinidos()
            }

            SesionEntrenamientoScreen(
                rutinaId = rutinaId,
                ejerciciosEnRutina = ejerciciosEnRutina,
                ejerciciosPredefinidos = ejerciciosPredefinidos,
                rutinaViewModel = rutinaViewModel,
                onBack = { navController.popBackStack() },
                onFinalizarSesion = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Pantalla de perfil del usuario donde se presentan opciones relacionadas a la cuenta.
 *
 * @param onMedidasClick Lambda que se ejecuta cuando el usuario presiona el botón para
 *                      navegar a la sección de medidas corporales.
 */
@Composable
fun PerfilScreen(
    onMedidasClick: () -> Unit
) {
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onMedidasClick,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Mis Medidas Corporales")
            }
        }
    }
}