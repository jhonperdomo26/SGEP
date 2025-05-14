package com.example.sgep.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.sgep.data.database.AppDatabase
import com.example.sgep.data.entity.UserEntity
import com.example.sgep.viewmodel.EjercicioViewModel
import com.example.sgep.viewmodel.EjercicioViewModelFactory
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val label: String, val icon: @Composable (() -> Unit)) {
    object Inicio : Screen("inicio", "Inicio", { Icon(Icons.Default.Home, contentDescription = "Inicio") })
    object Rutinas : Screen("rutinas", "Rutinas", { Icon(Icons.Default.FitnessCenter, contentDescription = "Rutinas") })
    object Perfil : Screen("perfil", "Perfil", { Icon(Icons.Default.Person, contentDescription = "Perfil") })
}

@Composable
fun MainScreen(user: UserEntity?, onLogout: () -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val database = AppDatabase.getDatabase(context)
    val rutinaDao = database.rutinaDao()
    val factory = EjercicioViewModelFactory(rutinaDao)

    val ejercicioViewModel: EjercicioViewModel = viewModel(factory = factory)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            user = user,
            onLogout = onLogout,
            ejercicioViewModel = ejercicioViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(Screen.Inicio, Screen.Rutinas, Screen.Perfil)
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { screen.icon() },
                label = { Text(screen.label) },
                selected = currentDestination?.route == screen.route,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    user: UserEntity?,
    onLogout: () -> Unit,
    ejercicioViewModel: EjercicioViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = Screen.Inicio.route) {
        composable(Screen.Inicio.route) { WelcomeScreen(user = user, onLogout = onLogout) }
        composable(Screen.Rutinas.route) {
            // *** Modificado para pasar ejercicioViewModel a RutinasScreen ***
            RutinasScreen(
                navController = navController,
                ejercicioViewModel = ejercicioViewModel
            )
        }
        composable(Screen.Perfil.route) { PerfilScreen(user = user) }

        composable("crearRutina") {
            CrearRutinaScreen(
                rutinaId = null, // Para crear una nueva rutina
                onAgregarEjercicio = { /* Implementar lógica de añadir ejercicio si es necesario externamente */ },
                onGuardarRutina = { rutina ->
                    ejercicioViewModel.guardarRutina(rutina)
                    navController.popBackStack()
                },
                ejercicioViewModel = ejercicioViewModel
            )
        }
    }
}
