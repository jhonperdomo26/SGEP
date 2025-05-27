package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.RegistroSerieSesionEntity

/**
 * DAO para registrar y consultar el desempeño real del usuario durante cada serie
 * en una sesión de entrenamiento.
 * Este registro permite analizar lo que el usuario efectivamente ejecutó.
 */
@Dao
interface RegistroSerieSesionDao {

    /**
     * Inserta un nuevo registro de serie realizada durante una sesión.
     *
     * @param registro Objeto [RegistroSerieSesionEntity] que representa la serie ejecutada.
     * @return ID generado automáticamente para el registro insertado.
     */
    @Insert
    suspend fun insert(registro: RegistroSerieSesionEntity): Long

    /**
     * Obtiene todos los registros relacionados a un ejercicio específico en todas las sesiones.
     *
     * @param ejercicioEnRutinaId ID del ejercicio dentro de la rutina.
     * @return Lista de [RegistroSerieSesionEntity] para ese ejercicio.
     */
    @Query("SELECT * FROM registro_serie_sesion WHERE ejercicioEnRutinaId = :ejercicioEnRutinaId")
    suspend fun getByEjercicioEnRutinaId(ejercicioEnRutinaId: Int): List<RegistroSerieSesionEntity>
}