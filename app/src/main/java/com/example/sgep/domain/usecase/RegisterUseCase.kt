package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.UserRepository

/**
 * Caso de uso para el registro y verificación de usuarios en el sistema.
 *
 * Proporciona funcionalidades para registrar nuevos usuarios
 * y para verificar si un correo electrónico ya está registrado.
 *
 * @property userRepository Repositorio para operaciones relacionadas con usuarios.
 */
class RegisterUseCase(private val userRepository: UserRepository) {

    /**
     * Registra un nuevo usuario con los datos proporcionados.
     *
     * @param nombre Nombre completo del usuario.
     * @param email Correo electrónico único del usuario.
     * @param password Contraseña segura para la cuenta.
     * @param objetivo Objetivo personal o meta del usuario (por ejemplo, "ganar músculo").
     * @return Result<Long> con el ID generado del usuario registrado si la operación es exitosa,
     *         o un Result.failure con el error ocurrido.
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
}