package com.example.sgep.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.database.AppDatabase
import com.example.sgep.data.entity.UserEntity
import com.example.sgep.domain.usecase.LoginUseCase
import com.example.sgep.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar el estado y la lógica relacionada con
 * el inicio y cierre de sesión del usuario.
 *
 * Provee estados reactivos que permiten a la UI observar cambios en el usuario actual
 * y los resultados de las operaciones de login y registro.
 *
 * @property application Contexto de la aplicación, requerido para acceso a la base de datos.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginUseCase: LoginUseCase

    /**
     * Estado observable que contiene el usuario actualmente autenticado.
     */
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> get() = _currentUser

    /**
     * Estado observable con mensajes relacionados al resultado del proceso de login.
     */
    private val _loginResult = MutableStateFlow("")
    val loginResult: StateFlow<String> get() = _loginResult

    init {
        val db = AppDatabase.getDatabase(application)
        val userRepository = UserRepository(db.userDao())
        loginUseCase = LoginUseCase(userRepository)

        // Cargar el usuario actualmente autenticado al iniciar el ViewModel
        viewModelScope.launch {
            loginUseCase.getCurrentUser().collect { user ->
                _currentUser.value = user
            }
        }
    }

    /**
     * Realiza el intento de inicio de sesión con las credenciales proporcionadas.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * Actualiza [loginResult] con mensajes de éxito o error según corresponda.
     */
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginResult.value = "Por favor, complete todos los campos."
            return
        }

        viewModelScope.launch {
            try {
                val user = loginUseCase.login(email, password)
                if (user != null) {
                    _currentUser.value = user
                    _loginResult.value = "Inicio de sesión exitoso"
                    Log.d("LoginViewModel", "Inicio de sesión exitoso para: ${user.nombre}")
                } else {
                    _loginResult.value = "Credenciales incorrectas"
                    Log.d("LoginViewModel", "Credenciales incorrectas para: $email")
                }
            } catch (e: Exception) {
                _loginResult.value = "Error inesperado: ${e.localizedMessage ?: "Error desconocido"}"
                Log.e("LoginViewModel", "Error en login: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Cierra la sesión del usuario actualmente autenticado,
     * limpiando los estados relacionados.
     */
    fun logout() {
        viewModelScope.launch {
            _currentUser.value = null
            _loginResult.value = ""
            Log.d("LoginViewModel", "Sesión cerrada")
        }
    }
}