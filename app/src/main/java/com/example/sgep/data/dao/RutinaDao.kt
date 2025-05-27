package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.RutinaEntity

/**
 * DAO para acceder y manipular las rutinas almacenadas en la base de datos.
 * Permite operaciones básicas como insertar, consultar, actualizar y eliminar rutinas.
 */
@Dao
interface RutinaDao {

    /**
     * Inserta una nueva rutina en la base de datos.
     *
     * @param rutina Objeto [RutinaEntity] que representa la rutina a registrar.
     * @return ID generado automáticamente para la rutina insertada.
     */
    @Insert
    suspend fun insertRutina(rutina: RutinaEntity): Long

    /**
     * Obtiene todas las rutinas registradas en la base de datos.
     *
     * @return Lista de [RutinaEntity] que representa todas las rutinas almacenadas.
     */
    @Query("SELECT * FROM rutina")
    suspend fun getAllRutinas(): List<RutinaEntity>

    /**
     * Obtiene una rutina específica según su ID.
     *
     * @param rutinaId ID de la rutina a buscar.
     * @return Objeto [RutinaEntity] si se encuentra, o null si no existe.
     */
    @Query("SELECT * FROM rutina WHERE id = :rutinaId")
    suspend fun getRutinaById(rutinaId: Int): RutinaEntity?

    /**
     * Obtiene todas las rutinas creadas por un usuario específico.
     *
     * @param userId ID del usuario cuyas rutinas se desean recuperar.
     * @return Lista de [RutinaEntity] asociadas al usuario indicado.
     */
    @Query("SELECT * FROM rutina WHERE userId = :userId")
    suspend fun getRutinasByUserId(userId: Int): List<RutinaEntity>

    /**
     * Actualiza los datos de una rutina existente en la base de datos.
     *
     * @param rutina Objeto [RutinaEntity] con los nuevos valores a actualizar.
     */
    @Update
    suspend fun updateRutina(rutina: RutinaEntity)

    /**
     * Elimina una rutina específica de la base de datos.
     *
     * @param rutina Objeto [RutinaEntity] que se desea eliminar.
     */
    @Delete
    suspend fun deleteRutina(rutina: RutinaEntity)
}