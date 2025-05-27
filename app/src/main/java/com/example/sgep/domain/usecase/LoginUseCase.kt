package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.UserRepository
import com.example.sgep.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para el manejo de autenticación de usuarios.
 */
class LoginUseCase(private val userRepository: UserRepository) {

    /**
     * Realiza la autenticación de un usuario usando correo y contraseña.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña en texto plano (sin hash).
     * @return Usuario autenticado si las credenciales son correctas, o null en caso contrario.
     */
    suspend fun login(email: String, password: String): UserEntity? {
        val user = userRepository.getUserByEmail(email)
        return if (user != null && user.contraseñaHash == password.hashCode().toString()) {
            user
        } else {
            null
        }
    }

    /**
     * Obtiene un Flow con el usuario actualmente logueado o null si no hay sesión activa.
     * La implementación devuelve el último usuario registrado o la sesión activa según el repositorio.
     *
     * @return Flow de UserEntity? representando el usuario actual.
     */
    fun getCurrentUser(): Flow<UserEntity?> = userRepository.getCurrentUserFlow()
}