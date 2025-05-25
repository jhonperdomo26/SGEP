package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.UserRepository
import com.example.sgep.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class LoginUseCase ( private val userRepository: UserRepository ) {
    /**
     * Realiza el proceso de autenticación del usuario.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña sin hashear.
     * @return El usuario autenticado o null si las credenciales son incorrectas.
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
     * Obtiene el usuario actualmente logueado (último usuario en la base de datos).
     * @return Flow con el usuario actual o null si no hay sesión activa.
     */
    fun getCurrentUser(): Flow<UserEntity?> {
        return userRepository.getCurrentUserFlow()
    }

    /**
     * Cierra la sesión actual del usuario.
     */
    suspend fun logout() {
        // En una implementación más avanzada, podríamos marcar explícitamente el logout
        // pero con el diseño actual, simplemente no tendremos un usuario "actual"
    }
}
