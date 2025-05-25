package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.SesionRutinaEntity

/**
 * Maneja las sesiones históricas de cada rutina (cada vez que un usuario entrena).
 */
@Dao
interface SesionRutinaDao {
    @Insert
    suspend fun insert(sesion: SesionRutinaEntity): Long

    @Query("SELECT * FROM sesion_rutina WHERE rutinaId = :rutinaId")
    suspend fun getByRutinaId(rutinaId: Int): List<SesionRutinaEntity>

    @Query("SELECT * FROM sesion_rutina WHERE id = :sesionId")
    suspend fun getById(sesionId: Int): SesionRutinaEntity?
}
