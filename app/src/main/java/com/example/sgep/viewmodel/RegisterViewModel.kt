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

/**
 * ViewModel encargado de manejar la lógica de registro de usuarios.
 *
 * Este ViewModel extiende [AndroidViewModel] para acceder al contexto de la aplicación.
 * Realiza validaciones de entrada, invoca el caso de uso de registro y expone
 * el resultado del registro mediante un [StateFlow].
 *
 * @property registerUseCase Caso de uso para registrar un usuario en el sistema.
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val registerUseCase: RegisterUseCase

    private val _registerResult = MutableStateFlow("")
    /**
     * Flujo que emite mensajes de resultado del proceso de registro.
     */
    val registerResult: StateFlow<String> get() = _registerResult

    // Patrón regex para validar el formato del correo electrónico.
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

    /**
     * Intenta registrar un usuario con los datos proporcionados.
     *
     * Realiza validaciones locales para nombre, email y contraseña antes de invocar el caso de uso.
     * Actualiza [registerResult] con mensajes de éxito o error según corresponda.
     *
     * @param nombre Nombre del usuario.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña ingresada.
     * @param confirmPassword Confirmación de la contraseña.
     * @param objetivo Objetivo o meta del usuario.
     */
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

    /**
     * Valida que el email tenga un formato correcto según [EMAIL_PATTERN].
     *
     * @param email Correo electrónico a validar.
     * @return `true` si el formato es válido, `false` en caso contrario.
     */
    private fun isValidEmail(email: String): Boolean {
        return EMAIL_PATTERN.matcher(email).matches()
    }

    /**
     * Limpia el mensaje de resultado de registro.
     */
    fun clearRegisterResult() {
        _registerResult.value = ""
    }
}