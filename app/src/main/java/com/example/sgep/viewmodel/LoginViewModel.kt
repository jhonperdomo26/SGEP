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

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginUseCase: LoginUseCase
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> get() = _currentUser

    private val _loginResult = MutableStateFlow("")
    val loginResult: StateFlow<String> get() = _loginResult

    private val _registerResult = MutableStateFlow("")
    val registerResult: StateFlow<String> get() = _registerResult

    init {
        val db = AppDatabase.getDatabase(application)
        val userRepository = UserRepository(db.userDao())
        loginUseCase = LoginUseCase(userRepository)

        // Cargar usuario actual al iniciar
        viewModelScope.launch {
            loginUseCase.getCurrentUser().collect { user ->
                _currentUser.value = user
            }
        }
    }

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

    fun logout() {
        viewModelScope.launch {
            _currentUser.value = null
            _loginResult.value = ""
            Log.d("LoginViewModel", "Sesión cerrada")
        }
    }
}