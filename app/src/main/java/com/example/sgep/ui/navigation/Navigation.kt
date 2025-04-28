package com.example.sgep.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sgep.ui.view.LoginScreen
import com.example.sgep.ui.view.RegisterScreen
import com.example.sgep.ui.view.WelcomeScreen
import com.example.sgep.viewmodel.LoginViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val WELCOME = "welcome"
}

@Composable
fun Navigation(viewModel: LoginViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        // Ruta para LoginScreen
        composable(Routes.LOGIN) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true } // Limpia el stack
                    }
                },
                viewModel = viewModel
            )
        }
        // Ruta para RegisterScreen
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        // Ruta para WelcomeScreen
        composable(Routes.WELCOME) {
            WelcomeScreen(
                user = TODO(),
                onLogout = TODO()
            )
        }
    }
}