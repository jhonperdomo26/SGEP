package com.example.sgep.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.sgep.data.entity.UserEntity
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val label: String, val icon: @Composable (() -> Unit)) {
    object Inicio : Screen("inicio", "Inicio", { Icon(Icons.Default.Home, contentDescription = "Inicio") })
    object Rutinas : Screen("rutinas", "Rutinas", { Icon(Icons.Default.FitnessCenter, contentDescription = "Rutinas") })
    object Perfil : Screen("perfil", "Perfil", { Icon(Icons.Default.Person, contentDescription = "Perfil") })
}

@Composable
fun MainScreen(user: UserEntity?, onLogout: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavigationHost(navController = navController, user = user, onLogout = onLogout, Modifier.padding(innerPadding))
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
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = Screen.Inicio.route) {
        composable(Screen.Inicio.route) { WelcomeScreen(user = user, onLogout = onLogout) }
        composable(Screen.Rutinas.route) { RutinasScreen() }
        composable(Screen.Perfil.route) { PerfilScreen(user = user) }
    }
}

@Composable
fun RutinasScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Pantalla de Rutinas", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun PerfilScreen(user: UserEntity?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Perfil del usuario: ${user?.nombre ?: "Usuario"}",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}