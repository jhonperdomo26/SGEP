package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.MedidaCorporalEntity

/**
 * MedidaCorporalDao permite acceder y modificar los registros de medidas corporales.
 */
@Dao
interface MedidaCorporalDao {

    // Insertar una nueva medida corporal
    @Insert
    suspend fun insertMedida(medida: MedidaCorporalEntity): Long

    // Obtener todas las medidas de un usuario, ordenadas por fecha descendente
    @Query("SELECT * FROM medida_corporal WHERE userId = :userId ORDER BY fecha DESC")
    suspend fun getMedidasByUser(userId: Int): List<MedidaCorporalEntity>

    // Obtener una medida por ID
    @Query("SELECT * FROM medida_corporal WHERE id = :medidaId")
    suspend fun getMedidaById(medidaId: Int): MedidaCorporalEntity?

    // Actualizar un registro de medida corporal
    @Update
    suspend fun updateMedida(medida: MedidaCorporalEntity)

    // Eliminar una medida corporal
    @Delete
    suspend fun deleteMedida(medida: MedidaCorporalEntity)

    //Obtener última medida ingresada por el usuario
    @Query("SELECT * FROM medida_corporal WHERE userId = :userId ORDER BY fecha DESC LIMIT 1")
    suspend fun getUltimaMedidaByUser(userId: Int): MedidaCorporalEntity?
}