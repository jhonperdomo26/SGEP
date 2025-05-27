package com.example.sgep.data.repository

import com.example.sgep.data.dao.*
import com.example.sgep.data.entity.*

/**
 * Repositorio que maneja toda la lógica relacionada con la creación,
 * consulta, actualización y eliminación de rutinas y sus ejercicios asociados.
 */
class RutinaRepository(
    private val rutinaDao: RutinaDao,
    private val ejercicioEnRutinaDao: EjercicioEnRutinaDao,
    private val serieEjercicioDao: SerieEjercicioDao,
    private val ejercicioPredefinidoDao: EjercicioPredefinidoDao
) {

    /**
     * Crea una nueva rutina para un usuario específico.
     *
     * @param nombre Nombre de la rutina.
     * @param userId ID del usuario propietario de la rutina.
     * @return ID generado de la nueva rutina.
     */
    suspend fun crearRutina(nombre: String, userId: Int): Long =
        rutinaDao.insertRutina(RutinaEntity(nombre = nombre, userId = userId))

    /**
     * Obtiene todas las rutinas almacenadas.
     *
     * @return Lista de todas las rutinas.
     */
    suspend fun obtenerRutinas(): List<RutinaEntity> =
        rutinaDao.getAllRutinas()

    /**
     * Obtiene una rutina específica por su ID.
     *
     * @param rutinaId ID de la rutina a buscar.
     * @return La rutina encontrada o null si no existe.
     */
    suspend fun obtenerRutinaPorId(rutinaId: Int): RutinaEntity? =
        rutinaDao.getRutinaById(rutinaId)

    /**
     * Obtiene todas las rutinas asociadas a un usuario específico.
     *
     * @param userId ID del usuario.
     * @return Lista de rutinas del usuario.
     */
    suspend fun obtenerRutinasPorUsuario(userId: Int): List<RutinaEntity> =
        rutinaDao.getRutinasByUserId(userId)

    /**
     * Elimina una rutina existente.
     *
     * @param rutina La rutina a eliminar.
     */
    suspend fun eliminarRutina(rutina: RutinaEntity) =
        rutinaDao.deleteRutina(rutina)

    /**
     * Obtiene todos los ejercicios predefinidos disponibles.
     *
     * @return Lista de ejercicios predefinidos.
     */
    suspend fun obtenerEjerciciosPredefinidos(): List<EjercicioPredefinidoEntity> =
        ejercicioPredefinidoDao.getAll()

    /**
     * Agrega un ejercicio predefinido a una rutina específica.
     *
     * @param rutinaId ID de la rutina.
     * @param ejercicioPredefinidoId ID del ejercicio predefinido.
     * @return ID generado del nuevo ejercicio agregado a la rutina.
     */
    suspend fun agregarEjercicioARutina(rutinaId: Int, ejercicioPredefinidoId: Int): Long =
        ejercicioEnRutinaDao.insert(
            EjercicioEnRutinaEntity(
                rutinaId = rutinaId,
                ejercicioPredefinidoId = ejercicioPredefinidoId
            )
        )

    /**
     * Obtiene todos los ejercicios que forman parte de una rutina.
     *
     * @param rutinaId ID de la rutina.
     * @return Lista de ejercicios en la rutina.
     */
    suspend fun obtenerEjerciciosDeRutina(rutinaId: Int): List<EjercicioEnRutinaEntity> =
        ejercicioEnRutinaDao.getByRutinaId(rutinaId)

    /**
     * Agrega una serie a un ejercicio dentro de una rutina.
     *
     * @param ejercicioEnRutinaId ID del ejercicio dentro de la rutina.
     * @param numeroSerie Número de la serie (orden).
     * @param peso Peso utilizado en la serie.
     * @param repeticiones Cantidad de repeticiones.
     * @param descanso Tiempo de descanso en segundos entre series.
     * @return ID generado de la nueva serie.
     */
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
}