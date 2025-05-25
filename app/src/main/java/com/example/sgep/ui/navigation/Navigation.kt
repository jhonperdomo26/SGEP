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
import com.example.sgep.viewmodel.RutinaViewModel
import com.example.sgep.viewmodel.MedidaCorporalViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN_WITH_USER = "main/{userId}" // ✅ Cambio 2
}

/**
 * Navigation: NavHost principal de la app. Solo maneja login, registro y main.
 * El RutinaViewModel se pasa desde aquí para que esté disponible en MainScreen y sus pantallas hijas.
 */
@Composable
fun Navigation(
    viewModel: LoginViewModel,
    rutinaViewModel: RutinaViewModel,
    medidaCorporalViewModel: MedidaCorporalViewModel
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = { user ->
                    Log.d("Navigation", "Navegando a MainScreen con usuario: ${user.nombre}")
                    // ✅ Cambio 1: navegación con userId
                    navController.navigate("main/${user.id}") {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(Routes.LOGIN) }
            )
        }

        // ✅ Cambio 3: nueva ruta que recibe userId como argumento
        composable("main/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
            Log.d("Navigation", "userId recibido en MainScreen: $userId")

            MainScreen(
                userId = userId,
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo("main/{userId}") { inclusive = true }
                    }
                },
                rutinaViewModel = rutinaViewModel,
                medidaCorporalViewModel = medidaCorporalViewModel
            )
        }
    }
}
