package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.RutinaEntity

/**
 * RutinaDao nos permite acceder y modificar las rutinas en la base de datos.
 */
@Dao
interface RutinaDao {
    // Insertar una nueva rutina
    @Insert
    suspend fun insertRutina(rutina: RutinaEntity): Long

    // Obtener todas las rutinas
    @Query("SELECT * FROM rutina")
    suspend fun getAllRutinas(): List<RutinaEntity>

    // Obtener una rutina específica por ID
    @Query("SELECT * FROM rutina WHERE id = :rutinaId")
    suspend fun getRutinaById(rutinaId: Int): RutinaEntity?

    // Actualizar una rutina existente
    @Update
    suspend fun updateRutina(rutina: RutinaEntity)

    // Eliminar una rutina por entidad
    @Delete
    suspend fun deleteRutina(rutina: RutinaEntity)
}
