package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.SesionRutinaRepository

/**
 * Caso de uso para registrar lo que el usuario hizo en una serie de una sesión.
 */
class RegistrarSerieSesionUseCase(private val sesionRutinaRepository: SesionRutinaRepository) {
    suspend operator fun invoke(
        sesionRutinaId: Int,
        ejercicioEnRutinaId: Int,
        numeroSerie: Int,
        peso: Float,
        repeticiones: Int,
        completada: Boolean
    ): Long = sesionRutinaRepository.registrarSerieEnSesion(
        sesionRutinaId,
        ejercicioEnRutinaId,
        numeroSerie,
        peso,
        repeticiones,
        completada
    )
}
