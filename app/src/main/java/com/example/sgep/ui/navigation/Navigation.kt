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

@Composable
fun Navigation(viewModel: LoginViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        // Ruta para LoginScreen
        composable("login") {
            LoginScreen(
                onRegisterClick = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("welcome") }, // Navegar a WelcomeScreen después de login
                viewModel = viewModel
            )
        }
        // Ruta para RegisterScreen
        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        // Ruta para WelcomeScreen
        composable("welcome") {
            WelcomeScreen()
        }
    }
}