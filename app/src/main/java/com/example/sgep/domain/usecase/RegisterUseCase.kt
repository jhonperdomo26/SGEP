package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class RegisterUseCase(private val userRepository: UserRepository) {
    /**
     * Registra un nuevo usuario en la base de datos.
     * @return Result<Long> con el ID del usuario registrado o un mensaje de error
     */
    suspend fun registerUser(
        nombre: String,
        email: String,
        password: String,
        objetivo: String
    ): Result<Long> {
        return userRepository.registerUser(
            nombre = nombre,
            email = email,
            password = password,
            objetivo = objetivo
        )
    }

    /**
     * Verifica si un email ya está registrado
     */
    suspend fun isEmailRegistered(email: String): Boolean {
        return userRepository.getUserByEmail(email) != null
    }
}