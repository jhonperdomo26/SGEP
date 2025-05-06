package com.example.sgep.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sgep.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user Objeto de tipo UserEntity que representa al usuario a registrar.
     * @return El ID del usuario registrado.
     */
    @Insert
    suspend fun registerUser(user: UserEntity): Long

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return El usuario encontrado o `null` si no existe.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return El usuario encontrado o `null` si no existe.
     */
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    /**
     * Obtiene un flujo de datos del usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return Un Flow que emite el usuario encontrado o `null` si no existe.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserFlowByEmail(email: String): Flow<UserEntity?>

    /**
     * Obtiene el usuario actualmente logeado (último usuario insertado)
     * Útil para flujos de autenticación persistente
     */
    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    /**
     * Actualiza los datos del usuario
     */
    @Update
    suspend fun updateUser(user: UserEntity): Int
}