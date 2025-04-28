package com.example.sgep.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.database.AppDatabase
import com.example.sgep.data.entity.UserEntity
import com.example.sgep.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> get() = _currentUser

    private val _loginResult = MutableStateFlow("")
    val loginResult: StateFlow<String> get() = _loginResult

    private val _registerResult = MutableStateFlow("")
    val registerResult: StateFlow<String> get() = _registerResult

    init {
        val db = AppDatabase.getDatabase(application)
        repository = UserRepository(db.userDao())
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginResult.value = "Por favor, complete todos los campos."
            return
        }

        viewModelScope.launch {
            try {
                val success = repository.validateLogin(email, password)
                _loginResult.value = if (success) "Inicio de sesión exitoso" else "Credenciales incorrectas"
            } catch (e: Exception) {
                _loginResult.value = "Error inesperado: ${e.message}"
            }
        }
    }

    fun register(
        nombre: String,
        email: String,
        password: String,
        pesoActual: Double?,
        estatura: Double?,
        objetivo: String
    ) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            _registerResult.value = "Por favor, complete todos los campos obligatorios."
            return
        }

        viewModelScope.launch {
            try {
                val result = repository.registerUser(nombre, email, password, pesoActual, estatura, objetivo)
                _registerResult.value = result.fold(
                    onSuccess = { "Registro exitoso" },
                    onFailure = { it.message ?: "Error al registrar" }
                )
            } catch (e: Exception) {
                _registerResult.value = "Error inesperado: ${e.message}"
            }
        }
    }

    fun updateRegisterErrorMessage(message: String) {
        _registerResult.value = message // Actualización para MutableStateFlow
    }

    fun logout() {
        _currentUser.value = null
        _loginResult.value = ""
    }
}