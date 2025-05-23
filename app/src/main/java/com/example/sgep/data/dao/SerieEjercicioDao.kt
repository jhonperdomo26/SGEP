package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.SerieEjercicioEntity

/**
 * Maneja el acceso a las series configuradas para cada ejercicio de la rutina.
 * Aquí puedes insertar, actualizar o borrar series.
 */
@Dao
interface SerieEjercicioDao {
    @Insert
    suspend fun insert(serie: SerieEjercicioEntity): Long

    @Query("SELECT * FROM serie_ejercicio WHERE ejercicioEnRutinaId = :ejercicioEnRutinaId")
    suspend fun getByEjercicioEnRutinaId(ejercicioEnRutinaId: Int): List<SerieEjercicioEntity>

    @Update
    suspend fun update(serie: SerieEjercicioEntity)

    @Delete
    suspend fun delete(serie: SerieEjercicioEntity)
}
