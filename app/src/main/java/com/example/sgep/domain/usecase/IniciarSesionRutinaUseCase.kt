package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.SesionRutinaRepository

/**
 * Caso de uso para iniciar una nueva sesión de rutina (historial).
 */
class IniciarSesionRutinaUseCase(private val sesionRutinaRepository: SesionRutinaRepository) {
    suspend operator fun invoke(rutinaId: Int): Long =
        sesionRutinaRepository.crearSesion(rutinaId)
}
