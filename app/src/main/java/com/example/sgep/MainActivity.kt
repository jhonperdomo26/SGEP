package com.example.sgep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sgep.ui.navigation.Navigation
import com.example.sgep.ui.theme.AppTheme
import com.example.sgep.viewmodel.LoginViewModel
import com.example.sgep.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // Crear instancia del LoginViewModel utilizando viewModel(...)
                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(application)
                )
                // Pasar loginViewModel a la función Navigation
                Navigation(viewModel = loginViewModel)
            }
        }
    }
}