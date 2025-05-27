package com.example.sgep.data.repository

import com.example.sgep.data.dao.*
import com.example.sgep.data.entity.*

/**
 * Repositorio encargado de manejar el historial de sesiones de entrenamiento (sesiones de rutina)
 * y el registro detallado de cada serie realizada durante dichas sesiones.
 *
 * @property sesionRutinaDao DAO para acceder a las sesiones de rutina.
 * @property registroSerieSesionDao DAO para acceder a los registros de series dentro de las sesiones.
 */
class SesionRutinaRepository(
    private val sesionRutinaDao: SesionRutinaDao,
    private val registroSerieSesionDao: RegistroSerieSesionDao
) {

    /**
     * Crea una nueva sesión de entrenamiento para una rutina dada.
     *
     * @param rutinaId ID de la rutina para la que se crea la sesión.
     * @return El ID de la nueva sesión insertada.
     */
    suspend fun crearSesion(rutinaId: Int): Long =
        sesionRutinaDao.insert(SesionRutinaEntity(rutinaId = rutinaId))

    /**
     * Registra los datos de una serie realizada durante una sesión de entrenamiento.
     *
     * @param sesionRutinaId ID de la sesión de rutina.
     * @param ejercicioEnRutinaId ID del ejercicio dentro de la rutina.
     * @param numeroSerie Número de la serie (orden).
     * @param peso Peso usado en la serie.
     * @param repeticiones Cantidad de repeticiones completadas.
     * @param completada Indica si la serie fue completada.
     * @return ID del registro insertado.
     */
    suspend fun registrarSerieEnSesion(
        sesionRutinaId: Int,
        ejercicioEnRutinaId: Int,
        numeroSerie: Int,
        peso: Float,
        repeticiones: Int,
        completada: Boolean
    ): Long =
        registroSerieSesionDao.insert(
            RegistroSerieSesionEntity(
                sesionRutinaId = sesionRutinaId,
                ejercicioEnRutinaId = ejercicioEnRutinaId,
                numeroSerie = numeroSerie,
                peso = peso,
                repeticiones = repeticiones,
                completada = completada
            )
        )

    /**
     * Obtiene todas las series registradas que corresponden a un ejercicio específico dentro de las sesiones.
     *
     * @param ejercicioEnRutinaId ID del ejercicio dentro de la rutina.
     * @return Lista de registros de series realizadas.
     */
    suspend fun obtenerSeriesPorEjercicio(ejercicioEnRutinaId: Int): List<RegistroSerieSesionEntity> =
        registroSerieSesionDao.getByEjercicioEnRutinaId(ejercicioEnRutinaId)
}