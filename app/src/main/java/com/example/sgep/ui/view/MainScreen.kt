package com.example.sgep.ui.view

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.sgep.data.entity.UserEntity
import com.example.sgep.viewmodel.LoginViewModel
import com.example.sgep.viewmodel.RutinaViewModel

sealed class Screen(val route: String, val label: String, val icon: @Composable (() -> Unit)) {
    object Inicio : Screen("inicio", "Inicio", { Icon(Icons.Default.Home, contentDescription = "Inicio") })
    object Rutinas : Screen("rutinas", "Rutinas", { Icon(Icons.Default.FitnessCenter, contentDescription = "Rutinas") })
    object Perfil : Screen("perfil", "Perfil", { Icon(Icons.Default.Person, contentDescription = "Perfil") })
}

@Composable
fun MainScreen(
    user: UserEntity?,
    loginViewModel: LoginViewModel,
    onLogout: () -> Unit,
    rutinaViewModel: RutinaViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            loginViewModel = loginViewModel,
            user = user,
            onLogout = onLogout,
            rutinaViewModel = rutinaViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

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

@Composable
fun NavigationHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    user: UserEntity?,
    onLogout: () -> Unit,
    rutinaViewModel: RutinaViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = Screen.Inicio.route, modifier = modifier) {
        composable(Screen.Inicio.route) {
            WelcomeScreen(
                user = user,
                viewModel = loginViewModel,
                onLogout = onLogout
            )
        }
        composable(Screen.Rutinas.route) {
            RutinasScreen(
                rutinaViewModel = rutinaViewModel,
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
            PerfilScreen(user = user)
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
                    onEliminarRutina = {
                        rutinaViewModel.eliminarRutina(it) {
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

@Composable
fun PerfilScreen(user: UserEntity?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Perfil del usuario: ${user?.nombre ?: "Usuario"}",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}