package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.SesionRutinaEntity

/**
 * DAO para acceder y gestionar las sesiones de rutina almacenadas en la base de datos.
 * Cada sesión representa una instancia de entrenamiento realizada por el usuario.
 */
@Dao
interface SesionRutinaDao {

    /**
     * Inserta una nueva sesión de rutina en la base de datos.
     *
     * @param sesion Objeto [SesionRutinaEntity] que representa la sesión realizada.
     * @return ID generado automáticamente para la sesión insertada.
     */
    @Insert
    suspend fun insert(sesion: SesionRutinaEntity): Long

    /**
     * Obtiene todas las sesiones registradas asociadas a una rutina específica.
     *
     * @param rutinaId ID de la rutina cuyas sesiones se desean recuperar.
     * @return Lista de [SesionRutinaEntity] asociadas a la rutina dada.
     */
    @Query("SELECT * FROM sesion_rutina WHERE rutinaId = :rutinaId")
    suspend fun getByRutinaId(rutinaId: Int): List<SesionRutinaEntity>

    /**
     * Recupera una sesión de rutina específica por su ID único.
     *
     * @param sesionId ID de la sesión que se desea obtener.
     * @return Objeto [SesionRutinaEntity] si se encuentra, o null si no existe.
     */
    @Query("SELECT * FROM sesion_rutina WHERE id = :sesionId")
    suspend fun getById(sesionId: Int): SesionRutinaEntity?
}