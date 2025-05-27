package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.SesionRutinaRepository

/**
 * Caso de uso para iniciar una nueva sesión de rutina.
 *
 * Recibe el ID de la rutina y delega la creación de la sesión en el repositorio correspondiente.
 */
class IniciarSesionRutinaUseCase(private val sesionRutinaRepository: SesionRutinaRepository) {

    /**
     * Inicia una nueva sesión para la rutina especificada.
     *
     * @param rutinaId ID de la rutina para la que se crea la sesión.
     * @return ID de la sesión recién creada.
     */
    suspend operator fun invoke(rutinaId: Int): Long =
        sesionRutinaRepository.crearSesion(rutinaId)
}