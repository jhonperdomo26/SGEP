package com.example.sgep.data.repository

import com.example.sgep.data.dao.*
import com.example.sgep.data.entity.*

/**
 * SesionRutinaRepository maneja el historial de entrenamientos (sesiones) y el registro real de cada serie.
 */
class SesionRutinaRepository(
    private val sesionRutinaDao: SesionRutinaDao,
    private val registroSerieSesionDao: RegistroSerieSesionDao
) {
    // Crear una nueva sesión
    suspend fun crearSesion(rutinaId: Int): Long =
        sesionRutinaDao.insert(SesionRutinaEntity(rutinaId = rutinaId))

    // Obtener sesiones pasadas por rutina
    suspend fun obtenerSesionesPorRutina(rutinaId: Int): List<SesionRutinaEntity> =
        sesionRutinaDao.getByRutinaId(rutinaId)

    // Registrar una serie durante una sesión
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

    // Obtener todas las series registradas para un ejercicio específico
    suspend fun obtenerSeriesPorEjercicio(ejercicioEnRutinaId: Int): List<RegistroSerieSesionEntity> =
        registroSerieSesionDao.getByEjercicioEnRutinaId(ejercicioEnRutinaId)
}