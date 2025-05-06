package com.example.sgep.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sgep.data.entity.EjercicioEntity

@Dao
interface EjercicioDao {

    @Insert
    suspend fun insertarEjercicio(ejercicio: EjercicioEntity)

    @Query("SELECT * FROM exercises")
    suspend fun obtenerTodosLosEjercicios(): List<EjercicioEntity>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun obtenerEjercicioPorId(id: Int): EjercicioEntity
}