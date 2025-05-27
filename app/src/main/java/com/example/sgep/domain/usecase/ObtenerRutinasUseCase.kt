package com.example.sgep.domain.usecase

import com.example.sgep.data.entity.EjercicioEnRutinaEntity
import com.example.sgep.data.entity.EjercicioPredefinidoEntity
import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.data.repository.RutinaRepository

/**
 * Caso de uso para obtener información relacionada con rutinas y ejercicios.
 *
 * Proporciona métodos para obtener todas las rutinas, rutinas de un usuario específico,
 * ejercicios dentro de una rutina y ejercicios predefinidos disponibles.
 *
 * @property rutinaRepository Repositorio para acceso a datos de rutinas y ejercicios.
 */
class ObtenerRutinasUseCase(private val rutinaRepository: RutinaRepository) {

    /**
     * Obtiene todas las rutinas disponibles en el sistema.
     *
     * @return Lista de todas las rutinas.
     */
    suspend operator fun invoke(): List<RutinaEntity> = rutinaRepository.obtenerRutinas()

    /**
     * Obtiene las rutinas asociadas a un usuario específico.
     *
     * @param userId ID del usuario.
     * @return Lista de rutinas pertenecientes al usuario.
     */
    suspend operator fun invoke(userId: Int): List<RutinaEntity> {
        return rutinaRepository.obtenerRutinasPorUsuario(userId)
    }

    /**
     * Obtiene los ejercicios que forman parte de una rutina específica.
     *
     * @param rutinaId ID de la rutina.
     * @return Lista de ejercicios en la rutina.
     */
    suspend fun obtenerEjerciciosDeRutina(rutinaId: Int): List<EjercicioEnRutinaEntity> {
        return rutinaRepository.obtenerEjerciciosDeRutina(rutinaId)
    }

    /**
     * Obtiene la lista de ejercicios predefinidos disponibles para agregar a rutinas.
     *
     * @return Lista de ejercicios predefinidos.
     */
    suspend fun obtenerEjerciciosPredefinidos(): List<EjercicioPredefinidoEntity> {
        return rutinaRepository.obtenerEjerciciosPredefinidos()
    }
}