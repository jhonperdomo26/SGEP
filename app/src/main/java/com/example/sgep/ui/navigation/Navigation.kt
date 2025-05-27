package com.example.sgep.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sgep.ui.view.LoginScreen
import com.example.sgep.ui.view.RegisterScreen
import com.example.sgep.ui.view.MainScreen
import com.example.sgep.viewmodel.LoginViewModel
import com.example.sgep.viewmodel.RegisterViewModel
import com.example.sgep.viewmodel.RutinaViewModel
import com.example.sgep.data.entity.UserEntity
import com.example.sgep.ui.view.EstadisticasEjercicioScreen
import com.example.sgep.viewmodel.MedidaCorporalViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Objeto que define las rutas de navegación dentro de la aplicación.
 */
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN_WITH_USER = "main/{userId}"
}

/**
 * Composable que configura la navegación principal de la aplicación utilizando NavHost.
 *
 * @param loginViewModel ViewModel que maneja la lógica de login y estado del usuario.
 * @param registerViewModel ViewModel encargado de la lógica y estado de registro de usuario.
 * @param rutinaViewModel ViewModel que gestiona las rutinas de entrenamiento.
 * @param medidaCorporalViewModel ViewModel encargado de las medidas corporales.
 *
 * Provee las rutas y las pantallas correspondientes:
 * - LoginScreen: Pantalla de inicio de sesión.
 * - RegisterScreen: Pantalla para registrar nuevos usuarios.
 * - MainScreen: Pantalla principal tras autenticación, recibe userId.
 * - EstadisticasEjercicioScreen: Pantalla para mostrar estadísticas de un ejercicio en particular.
 */
@Composable
fun Navigation(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    rutinaViewModel: RutinaViewModel,
    medidaCorporalViewModel: MedidaCorporalViewModel
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        // Pantalla de login
        composable(Routes.LOGIN) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = { user ->
                    // Guardar usuario en el back stack para poder recuperarlo en pantallas siguientes
                    navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
                    // Navegar a la pantalla principal con el userId y limpiar la pila de navegación del login
                    navController.navigate("main/${user.id}") {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                viewModel = loginViewModel
            )
        }

        // Pantalla de registro de usuario
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = registerViewModel,
                onBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    // Volver a login y limpiar la pila de registro para evitar volver atrás
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla principal, recibe userId como parámetro
        composable("main/{userId}") { backStackEntry ->
            val user = navController.previousBackStackEntry?.savedStateHandle?.get<UserEntity>("user")
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
            Log.d("Navigation", "userId recibido en MainScreen: $userId")

            MainScreen(
                userId = userId,
                user = user,
                loginViewModel = loginViewModel,
                onLogout = {
                    loginViewModel.logout()
                    // Navegar a login limpiando la pila de la pantalla principal
                    navController.navigate(Routes.LOGIN) {
                        popUpTo("main/{userId}") { inclusive = true }
                    }
                },
                rutinaViewModel = rutinaViewModel,
                medidaCorporalViewModel = medidaCorporalViewModel
            )
        }

        // Pantalla para mostrar estadísticas de un ejercicio específico en una rutina
        composable(
            "estadisticas_ejercicio/{ejercicioEnRutinaId}/{nombreEjercicio}/{grupoMuscular}/{descripcion}"
        ) { backStackEntry ->
            val ejercicioEnRutinaId = backStackEntry.arguments?.getString("ejercicioEnRutinaId")?.toIntOrNull()
                ?: return@composable

            val nombreEjercicio = URLDecoder.decode(
                backStackEntry.arguments?.getString("nombreEjercicio") ?: "Ejercicio",
                StandardCharsets.UTF_8.toString()
            )
            val grupoMuscular = URLDecoder.decode(
                backStackEntry.arguments?.getString("grupoMuscular") ?: "Grupo muscular",
                StandardCharsets.UTF_8.toString()
            )
            val descripcion = URLDecoder.decode(
                backStackEntry.arguments?.getString("descripcion") ?: "Descripción no disponible",
                StandardCharsets.UTF_8.toString()
            )

            EstadisticasEjercicioScreen(
                ejercicioEnRutinaId = ejercicioEnRutinaId,
                nombreEjercicio = nombreEjercicio,
                grupoMuscular = grupoMuscular,
                descripcion = descripcion,
                rutinaViewModel = rutinaViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}