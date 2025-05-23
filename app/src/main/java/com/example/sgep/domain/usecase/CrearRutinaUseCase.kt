package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.RutinaRepository

/**
 * Caso de uso para crear una rutina.
 */
class CrearRutinaUseCase(private val rutinaRepository: RutinaRepository) {
    suspend operator fun invoke(nombre: String): Long =
        rutinaRepository.crearRutina(nombre)
}