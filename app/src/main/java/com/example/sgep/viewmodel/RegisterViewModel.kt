package com.example.sgep.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.database.AppDatabase
import com.example.sgep.data.repository.UserRepository
import com.example.sgep.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val registerUseCase: RegisterUseCase
    private val _registerResult = MutableStateFlow("")
    val registerResult: StateFlow<String> get() = _registerResult

    // Patrón para validar email
    private val EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    init {
        val db = AppDatabase.getDatabase(application)
        val userRepository = UserRepository(db.userDao())
        registerUseCase = RegisterUseCase(userRepository)
    }

    fun register(
        nombre: String,
        email: String,
        password: String,
        confirmPassword: String,
        objetivo: String
    ) {
        // Validación de campos vacíos
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            _registerResult.value = "Nombre, email y contraseña son obligatorios"
            return
        }

        // Validación de formato de email
        if (!isValidEmail(email)) {
            _registerResult.value = "Por favor ingresa un correo electrónico válido (ejemplo: usuario@dominio.com)"
            return
        }

        // Validación de longitud de contraseña
        if (password.length < 4 || password.length > 8) {
            _registerResult.value = "La contraseña debe tener entre 4 y 8 caracteres"
            return
        }

        // Validación de coincidencia de contraseñas
        if (password != confirmPassword) {
            _registerResult.value = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            try {
                val result = registerUseCase.registerUser(
                    nombre = nombre,
                    email = email,
                    password = password,
                    objetivo = objetivo
                )

                if (result.isSuccess) {
                    _registerResult.value = "Registro exitoso"
                } else {
                    _registerResult.value = result.exceptionOrNull()?.message ?: "Error en el registro"
                }
            } catch (e: Exception) {
                _registerResult.value = "Error inesperado: ${e.localizedMessage}"
            }
        }
    }
    // Función para validar el formato del email
    private fun isValidEmail(email: String): Boolean {
        return EMAIL_PATTERN.matcher(email).matches()
    }

    fun clearRegisterResult() {
        _registerResult.value = ""
    }
}