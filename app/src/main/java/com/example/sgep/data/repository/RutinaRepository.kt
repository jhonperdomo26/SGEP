package com.example.sgep.data.repository

import com.example.sgep.data.dao.*
import com.example.sgep.data.entity.*

/**
 * RutinaRepository maneja todo lo relacionado con la creación, consulta y edición de rutinas.
 */
class RutinaRepository(
    private val rutinaDao: RutinaDao,
    private val ejercicioEnRutinaDao: EjercicioEnRutinaDao,
    private val serieEjercicioDao: SerieEjercicioDao,
    private val ejercicioPredefinidoDao: EjercicioPredefinidoDao
) {
    // Crear una nueva rutina para un usuario específico
    suspend fun crearRutina(nombre: String, userId: Int): Long =
        rutinaDao.insertRutina(RutinaEntity(nombre = nombre, userId = userId))

    // Obtener todas las rutinas
    suspend fun obtenerRutinas(): List<RutinaEntity> =
        rutinaDao.getAllRutinas()

    // Obtener una rutina específica por ID
    suspend fun obtenerRutinaPorId(rutinaId: Int): RutinaEntity? =
        rutinaDao.getRutinaById(rutinaId)

    // ✅ Obtener rutinas de un usuario específico
    suspend fun obtenerRutinasPorUsuario(userId: Int): List<RutinaEntity> =
        rutinaDao.getRutinasByUserId(userId)

    // Eliminar una rutina
    suspend fun eliminarRutina(rutina: RutinaEntity) =
        rutinaDao.deleteRutina(rutina)

    // Obtener todos los ejercicios predefinidos
    suspend fun obtenerEjerciciosPredefinidos(): List<EjercicioPredefinidoEntity> =
        ejercicioPredefinidoDao.getAll()

    // Agregar un ejercicio a una rutina
    suspend fun agregarEjercicioARutina(rutinaId: Int, ejercicioPredefinidoId: Int): Long =
        ejercicioEnRutinaDao.insert(
            EjercicioEnRutinaEntity(
                rutinaId = rutinaId,
                ejercicioPredefinidoId = ejercicioPredefinidoId
            )
        )

    // Obtener los ejercicios de una rutina
    suspend fun obtenerEjerciciosDeRutina(rutinaId: Int): List<EjercicioEnRutinaEntity> =
        ejercicioEnRutinaDao.getByRutinaId(rutinaId)

    // Agregar una serie a un ejercicio en rutina
    suspend fun agregarSerie(
        ejercicioEnRutinaId: Int,
        numeroSerie: Int,
        peso: Float,
        repeticiones: Int,
        descanso: Int
    ): Long =
        serieEjercicioDao.insert(
            SerieEjercicioEntity(
                ejercicioEnRutinaId = ejercicioEnRutinaId,
                numeroSerie = numeroSerie,
                peso = peso,
                repeticiones = repeticiones,
                descanso = descanso
            )
        )

    // Obtener todas las series de un ejercicio en rutina
    suspend fun obtenerSeriesDeEjercicio(ejercicioEnRutinaId: Int): List<SerieEjercicioEntity> =
        serieEjercicioDao.getByEjercicioEnRutinaId(ejercicioEnRutinaId)
}