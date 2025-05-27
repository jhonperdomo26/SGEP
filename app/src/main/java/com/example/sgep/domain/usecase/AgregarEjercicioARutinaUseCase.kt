package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.RutinaRepository

/**
 * Caso de uso para agregar un ejercicio predefinido a una rutina existente.
 *
 * @param rutinaId ID de la rutina a la que se agregará el ejercicio.
 * @param ejercicioPredefinidoId ID del ejercicio predefinido que se agregará.
 * @return ID del ejercicio agregado en la rutina.
 */
class AgregarEjercicioARutinaUseCase(private val rutinaRepository: RutinaRepository) {
    /**
     * Ejecuta la acción de agregar un ejercicio predefinido a una rutina.
     */
    suspend operator fun invoke(rutinaId: Int, ejercicioPredefinidoId: Int): Long =
        rutinaRepository.agregarEjercicioARutina(rutinaId, ejercicioPredefinidoId)
}