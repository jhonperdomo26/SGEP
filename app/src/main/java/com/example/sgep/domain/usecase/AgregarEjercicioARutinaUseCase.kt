package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.RutinaRepository

/**
 * Caso de uso para agregar un ejercicio predefinido a una rutina.
 */
class AgregarEjercicioARutinaUseCase(private val rutinaRepository: RutinaRepository) {
    suspend operator fun invoke(rutinaId: Int, ejercicioPredefinidoId: Int): Long =
        rutinaRepository.agregarEjercicioARutina(rutinaId, ejercicioPredefinidoId)
}
