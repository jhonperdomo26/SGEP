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

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN_WITH_USER = "main/{userId}"
}

/**
 * Navigation: NavHost principal de la app. Solo maneja login, registro y main.
 * El RutinaViewModel se pasa desde aquí para que esté disponible en MainScreen y sus pantallas hijas.
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
        composable(Routes.LOGIN) {
            LoginScreen(

                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = { user ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
                    navController.navigate("main/${user.id}") {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                viewModel = loginViewModel
            )
        }

        // Pantalla de Registro
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = registerViewModel, // Usar el ViewModel específico para registro

                // Volver atrás (a login) cuando se hace clic en "Ya tengo cuenta"
                onBack = { navController.popBackStack() },

                // Manejar registro exitoso:
                // 1. Navegar a pantalla de login
                // 2. Eliminar pantalla de registro del back stack
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        // ✅ Cambio 3: nueva ruta que recibe userId como argumento
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
                    navController.navigate(Routes.LOGIN) {
                        popUpTo("main/{userId}") { inclusive = true }
                    }
                },
                rutinaViewModel = rutinaViewModel,
                medidaCorporalViewModel = medidaCorporalViewModel
            )
        }

        composable("estadisticas_ejercicio/{ejercicioEnRutinaId}/{nombreEjercicio}/{grupoMuscular}/{descripcion}") { backStackEntry ->
            val ejercicioEnRutinaId = backStackEntry.arguments?.getString("ejercicioEnRutinaId")?.toIntOrNull() ?: return@composable

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
