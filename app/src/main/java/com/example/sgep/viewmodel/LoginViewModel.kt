package com.example.sgep.viewmodel

import android.app.Application
import android.util.Log
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
                // Verificar si el usuario existe en la base de datos
                val user = repository.getUserByEmail(email)
                if (user != null) {
                    if (user.contraseñaHash == password.hashCode().toString()) {
                        _currentUser.value = user // Actualizar el usuario actual
                        _loginResult.value = "Inicio de sesión exitoso"
                        Log.d("LoginViewModel", "Inicio de sesión exitoso para el usuario: ${user.nombre}")
                    } else {
                        _loginResult.value = "Credenciales incorrectas"
                        Log.d("LoginViewModel", "Contraseña incorrecta para el email: $email")
                    }
                } else {
                    _loginResult.value = "Usuario no encontrado"
                    Log.d("LoginViewModel", "Usuario no encontrado para el email: $email")
                }
            } catch (e: Exception) {
                _loginResult.value = "Error inesperado: ${e.localizedMessage ?: "Error desconocido"}"
                Log.e("LoginViewModel", "Error en login: ${e.localizedMessage}")
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
        viewModelScope.launch {
            try {
                val result = repository.registerUser(
                    nombre = nombre,
                    email = email,
                    password = password,
                    pesoActual = pesoActual,
                    estatura = estatura,
                    objetivo = objetivo
                )
                if (result.isSuccess) {
                    _registerResult.value = "Registro exitoso"
                    Log.d("LoginViewModel", "Registro exitoso para el usuario: $nombre")
                } else {
                    _registerResult.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                    Log.d("LoginViewModel", "Error en registro: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _registerResult.value = "Error inesperado: ${e.localizedMessage}"
                Log.e("LoginViewModel", "Error en registro: ${e.localizedMessage}")
            }
        }
    }

    fun updateRegisterErrorMessage(message: String) {
        _registerResult.value = message
    }

    fun logout() {
        _currentUser.value = null
        _loginResult.value = ""
        Log.d("LoginViewModel", "Sesión cerrada, usuario actual reiniciado a null")
    }
}