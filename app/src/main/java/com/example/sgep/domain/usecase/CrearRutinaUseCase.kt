package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.RutinaRepository

/**
 * Caso de uso para crear una nueva rutina para un usuario.
 *
 * @param nombre Nombre de la rutina a crear.
 * @param userId ID del usuario al que pertenece la rutina.
 * @return ID de la rutina creada.
 */
class CrearRutinaUseCase(private val rutinaRepository: RutinaRepository) {

    /**
     * Ejecuta la creación de la rutina.
     *
     * @param nombre Nombre de la rutina.
     * @param userId ID del usuario dueño de la rutina.
     * @return Long ID generado de la nueva rutina.
     */
    suspend operator fun invoke(nombre: String, userId: Int): Long =
        rutinaRepository.crearRutina(nombre, userId)
}