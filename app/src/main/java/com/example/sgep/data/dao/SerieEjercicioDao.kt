package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.SerieEjercicioEntity

/**
 * DAO para gestionar las series configuradas para cada ejercicio dentro de una rutina.
 * Permite insertar, actualizar y eliminar series específicas.
 */
@Dao
interface SerieEjercicioDao {

    /**
     * Inserta una nueva serie de ejercicio en la base de datos.
     *
     * @param serie Objeto [SerieEjercicioEntity] que representa la serie a registrar.
     * @return ID generado automáticamente para la serie insertada.
     */
    @Insert
    suspend fun insert(serie: SerieEjercicioEntity): Long

    /**
     * Actualiza los datos de una serie existente en la base de datos.
     *
     * @param serie Objeto [SerieEjercicioEntity] con los nuevos valores a actualizar.
     */
    @Update
    suspend fun update(serie: SerieEjercicioEntity)

    /**
     * Elimina una serie de ejercicio específica de la base de datos.
     *
     * @param serie Objeto [SerieEjercicioEntity] que se desea eliminar.
     */
    @Delete
    suspend fun delete(serie: SerieEjercicioEntity)
}