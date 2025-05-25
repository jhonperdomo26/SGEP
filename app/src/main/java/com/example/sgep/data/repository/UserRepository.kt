package com.example.sgep.data.repository

import com.example.sgep.data.dao.UserDao
import com.example.sgep.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return El usuario encontrado o `null` si no existe.
     */
    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    /**
     * Valida las credenciales de inicio de sesión.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña sin hashear.
     * @return `true` si las credenciales son válidas, de lo contrario `false`.
     */
    suspend fun validateLogin(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email)
        // Simula la validación de la contraseña hasheada, reemplázalo con la lógica real de hash.
        return user != null && user.contraseñaHash == password.hashCode().toString()
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param nombre Nombre del usuario.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña sin hashear.
     * @param pesoActual (Opcional) Peso actual del usuario.
     * @param estatura (Opcional) Estatura del usuario.
     * @param objetivo Objetivo del usuario.
     * @return Resultado de la operación: Success o Failure.
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
            contraseñaHash = password.hashCode().toString(), // Almacena el hash de la contraseña
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
     * Obtiene un flujo del usuario actualmente logueado.
     * @return Flow que emite el usuario actual o null si no hay sesión activa.
     */
    fun getCurrentUserFlow(): Flow<UserEntity?> {
        return userDao.getCurrentUser()
    }
}