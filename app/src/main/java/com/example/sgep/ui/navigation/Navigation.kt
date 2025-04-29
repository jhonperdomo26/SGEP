package com.example.sgep.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sgep.ui.view.LoginScreen
import com.example.sgep.ui.view.RegisterScreen
import com.example.sgep.ui.view.WelcomeScreen
import com.example.sgep.viewmodel.LoginViewModel
import com.example.sgep.data.entity.UserEntity

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val WELCOME = "welcome"
}

@Composable
fun Navigation(viewModel: LoginViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = { user ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
                    Log.d("Navigation", "Navegando a WelcomeScreen con usuario: ${user.nombre}")
                    navController.navigate(Routes.WELCOME) {
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
        composable(Routes.WELCOME) {
            val user = navController.previousBackStackEntry?.savedStateHandle?.get<UserEntity>("user")
            Log.d("Navigation", "Usuario recibido en WelcomeScreen: $user")
            WelcomeScreen(
                user = user,
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }
    }
}