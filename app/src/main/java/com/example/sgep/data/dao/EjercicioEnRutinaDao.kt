package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.EjercicioEnRutinaEntity

/**
 * Este DAO sirve para manejar la relación de ejercicios dentro de cada rutina.
 * Así puedes agregar, quitar o listar los ejercicios de una rutina.
 */
@Dao
interface EjercicioEnRutinaDao {
    @Insert
    suspend fun insert(ejercicioEnRutina: EjercicioEnRutinaEntity): Long

    @Query("SELECT * FROM ejercicios_en_rutina WHERE rutinaId = :rutinaId")
    suspend fun getByRutinaId(rutinaId: Int): List<EjercicioEnRutinaEntity>

    @Delete
    suspend fun delete(ejercicioEnRutina: EjercicioEnRutinaEntity)
}