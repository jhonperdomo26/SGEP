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

/**
 * Objeto que define las rutas principales de la aplicación.
 * Las rutas internas (de rutinas, detalles, etc.) se manejan dentro de MainScreen.
 */
object Routes {
    const val LOGIN = "login"       // Ruta para la pantalla de inicio de sesión
    const val REGISTER = "register" // Ruta para la pantalla de registro
    const val MAIN = "main"         // Ruta para la pantalla principal (después de login/registro)
}

/**
 * Componente principal de navegación de la aplicación.
 * Maneja la navegación entre las pantallas principales: login, registro y main.
 * El RutinaViewModel se pasa para que esté disponible en MainScreen y sus pantallas hijas.
 *
 * @param loginViewModel ViewModel para la pantalla de login
 * @param registerViewModel ViewModel para la pantalla de registro (nuevo)
 * @param rutinaViewModel ViewModel para las rutinas de entrenamiento
 */
@Composable
fun Navigation(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    rutinaViewModel: RutinaViewModel
) {
    // Controlador de navegación principal
    val navController: NavHostController = rememberNavController()

    // Configuración del grafo de navegación
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN // Pantalla inicial
    ) {
        // Pantalla de Login
        composable(Routes.LOGIN) {
            LoginScreen(
                // Navegar a pantalla de registro cuando se hace clic en el botón correspondiente
                onRegisterClick = { navController.navigate(Routes.REGISTER) },

                // Manejar login exitoso:
                // 1. Guardar usuario en el back stack
                // 2. Navegar a pantalla principal
                // 3. Eliminar pantalla de login del back stack
                onLoginSuccess = { user ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
                    Log.d("Navigation", "Navegando a MainScreen con usuario: ${user.nombre}")
                    navController.navigate(Routes.MAIN) {
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

        // Pantalla Principal (Main)
        composable(Routes.MAIN) {
            // Recuperar el usuario guardado en el back stack durante el login
            val user = navController.previousBackStackEntry?.savedStateHandle?.get<UserEntity>("user")
            Log.d("Navigation", "Usuario recibido en MainScreen: $user")

            MainScreen(
                user = user, // Pasar el usuario a MainScreen
                loginViewModel = loginViewModel,

                // Manejar logout:
                // 1. Cerrar sesión en el ViewModel
                // 2. Navegar a pantalla de login
                // 3. Eliminar pantalla principal del back stack
                onLogout = {
                    loginViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                },

                // Pasar el ViewModel de rutinas para que esté disponible en MainScreen y sus hijos
                rutinaViewModel = rutinaViewModel
            )
        }
    }
}