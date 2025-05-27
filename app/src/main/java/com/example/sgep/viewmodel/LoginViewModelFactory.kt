package com.example.sgep.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory para crear instancias de [LoginViewModel] que requieren un [Application].
 *
 * Esta fábrica permite inyectar el contexto de la aplicación en el ViewModel,
 * facilitando el acceso a recursos o bases de datos que requieran contexto.
 *
 * @property application Contexto de la aplicación.
 */
class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application) as T
        }
        throw IllegalArgumentException("Clase desconocida para ViewModel: ${modelClass.name}")
    }
}