package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.EjercicioEnRutinaEntity

/**
 * DAO para gestionar la relación entre ejercicios y rutinas.
 * Permite insertar, consultar y eliminar ejercicios asignados a una rutina específica.
 */
@Dao
interface EjercicioEnRutinaDao {

    /**
     * Inserta un nuevo ejercicio dentro de una rutina.
     *
     * @param ejercicioEnRutina Objeto [EjercicioEnRutinaEntity] que representa la relación.
     * @return ID generado para el nuevo registro.
     */
    @Insert
    suspend fun insert(ejercicioEnRutina: EjercicioEnRutinaEntity): Long

    /**
     * Obtiene todos los ejercicios asociados a una rutina específica.
     *
     * @param rutinaId ID de la rutina.
     * @return Lista de [EjercicioEnRutinaEntity] correspondientes a la rutina.
     */
    @Query("SELECT * FROM ejercicios_en_rutina WHERE rutinaId = :rutinaId")
    suspend fun getByRutinaId(rutinaId: Int): List<EjercicioEnRutinaEntity>
}