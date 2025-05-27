package com.example.sgep.data.repository

import com.example.sgep.data.dao.UserDao
import com.example.sgep.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de manejar la lógica de acceso y validación de usuarios.
 * Se comunica con el [UserDao] para interactuar con la base de datos local.
 *
 * @param userDao DAO que proporciona operaciones CRUD sobre usuarios.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email Correo del usuario.
     * @return [UserEntity] si existe, o null si no se encuentra.
     */
    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    /**
     * Registra un nuevo usuario en el sistema tras verificar que los datos sean válidos
     * y que el usuario no esté previamente registrado.
     *
     * @param nombre Nombre del usuario.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param objetivo Objetivo personal del usuario (e.g. perder grasa, ganar masa muscular).
     * @return Resultado exitoso con el ID del nuevo usuario o fallo con el motivo del error.
     */
    suspend fun registerUser(
        nombre: String,
        email: String,
        password: String,
        objetivo: String
    ): Result<Long> {
        if (email.isBlank() || password.isBlank() || nombre.isBlank()) {
            return Result.failure(Exception("Los campos nombre, email y contraseña son obligatorios."))
        }
        val existingUser = getUserByEmail(email)
        if (existingUser != null) {
            return Result.failure(Exception("El usuario ya está registrado."))
        }
        val user = UserEntity(
            nombre = nombre,
            email = email,
            contraseñaHash = password.hashCode().toString(),
            objetivo = objetivo
        )
        return try {
            val userId = userDao.registerUser(user)
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Devuelve un flujo reactivo con el usuario actual observado en la base de datos.
     *
     * @return Un [Flow] que emite el [UserEntity] actual, o null si no existe.
     */
    fun getCurrentUserFlow(): Flow<UserEntity?> {
        return userDao.getCurrentUser()
    }
}