package com.example.sgep.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sgep.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Proporciona acceso a la base de datos para operaciones relacionadas con usuarios.
 *
 * Este DAO maneja todas las operaciones CRUD para la entidad [UserEntity], incluyendo
 * autenticación, registro y actualización de perfiles. Utiliza corrutinas para operaciones
 * bloqueantes y Flow para observación reactiva de datos.
 */
@Dao
interface UserDao {

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user Datos completos del usuario a registrar.
     * @return El ID asignado al nuevo usuario (Long). -1 si falla la operación.
     * @throws android.database.sqlite.SQLiteConstraintException Si el email ya existe (debe ser único).
     */
    @Insert
    suspend fun registerUser(user: UserEntity): Long

    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * @param email Dirección de email a buscar (case-sensitive).
     * @return El [UserEntity] completo o `null` si no se encuentra.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Obtiene un usuario por su ID único.
     *
     * @param id Identificador del usuario (autogenerado).
     * @return El [UserEntity] completo o `null` si no existe.
     */
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    /**
     * Observa reactivamente los cambios en un usuario específico por email.
     *
     * @param email Email del usuario a observar.
     * @return Un [Flow] que emite automáticamente el [UserEntity] cuando hay cambios.
     *         Emite `null` si el usuario no existe o es eliminado.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserFlowByEmail(email: String): Flow<UserEntity?>

    /**
     * Observa reactivamente el último usuario registrado en el sistema.
     *
     * Útil para implementar:
     * - Autologin al reiniciar la app
     * - Mantener sesión activa
     *
     * @return Un [Flow] que emite el [UserEntity] más reciente o `null` si no hay usuarios.
     */
    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param user Objeto [UserEntity] con los datos actualizados (debe contener un ID válido).
     * @return Número de filas afectadas (1 si éxito, 0 si falla).
     * @throws IllegalArgumentException Si el ID del usuario no existe.
     */
    @Update
    suspend fun updateUser(user: UserEntity): Int
}