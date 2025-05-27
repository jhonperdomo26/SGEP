package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.MedidaCorporalEntity

/**
 * DAO para acceder y gestionar los registros de medidas corporales del usuario.
 * Permite insertar, consultar, actualizar y eliminar medidas corporales históricas.
 */
@Dao
interface MedidaCorporalDao {

    /**
     * Inserta un nuevo registro de medida corporal en la base de datos.
     *
     * @param medida Objeto [MedidaCorporalEntity] que representa las medidas del usuario.
     * @return ID generado automáticamente para la medida insertada.
     */
    @Insert
    suspend fun insertMedida(medida: MedidaCorporalEntity): Long

    /**
     * Obtiene todas las medidas corporales registradas por un usuario,
     * ordenadas de la más reciente a la más antigua.
     *
     * @param userId ID del usuario cuyas medidas se desean consultar.
     * @return Lista de [MedidaCorporalEntity] ordenadas por fecha descendente.
     */
    @Query("SELECT * FROM medida_corporal WHERE userId = :userId ORDER BY fecha DESC")
    suspend fun getMedidasByUser(userId: Int): List<MedidaCorporalEntity>

    /**
     * Obtiene un registro de medida corporal específico por su ID.
     *
     * @param medidaId ID de la medida corporal a recuperar.
     * @return Objeto [MedidaCorporalEntity] si se encuentra, o null si no existe.
     */
    @Query("SELECT * FROM medida_corporal WHERE id = :medidaId")
    suspend fun getMedidaById(medidaId: Int): MedidaCorporalEntity?

    /**
     * Actualiza un registro existente de medida corporal en la base de datos.
     *
     * @param medida Objeto [MedidaCorporalEntity] con los nuevos datos.
     */
    @Update
    suspend fun updateMedida(medida: MedidaCorporalEntity)

    /**
     * Elimina un registro específico de medida corporal.
     *
     * @param medida Objeto [MedidaCorporalEntity] que se desea eliminar.
     */
    @Delete
    suspend fun deleteMedida(medida: MedidaCorporalEntity)

    /**
     * Recupera la última medida corporal registrada por un usuario.
     *
     * @param userId ID del usuario.
     * @return La medida corporal más reciente, o null si no existen registros.
     */
    @Query("SELECT * FROM medida_corporal WHERE userId = :userId ORDER BY fecha DESC LIMIT 1")
    suspend fun getUltimaMedidaByUser(userId: Int): MedidaCorporalEntity?
}