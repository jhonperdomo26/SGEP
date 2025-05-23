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
import com.example.sgep.data.entity.UserEntity

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main"
    // Las rutas internas (rutinas, detalles, etc.) se manejan dentro de MainScreen
}

/**
 * Navigation: NavHost principal de la app. Solo maneja login, registro y main.
 * El RutinaViewModel se pasa desde aquí para que esté disponible en MainScreen y sus pantallas hijas.
 */
@Composable
fun Navigation(
    viewModel: LoginViewModel,
    rutinaViewModel: RutinaViewModel // <- Agregado aquí
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = { user ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
                    Log.d("Navigation", "Navegando a MainScreen con usuario: ${user.nombre}")
                    navController.navigate(Routes.MAIN) {
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
        composable(Routes.MAIN) {
            // Recuperar el usuario desde el back stack (como ya lo hacías)
            val user = navController.previousBackStackEntry?.savedStateHandle?.get<UserEntity>("user")
            Log.d("Navigation", "Usuario recibido en MainScreen: $user")
            // Pasa el rutinaViewModel a MainScreen
            MainScreen(
                user = user,
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                },
                rutinaViewModel = rutinaViewModel
            )
        }
    }
}
