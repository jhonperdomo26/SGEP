package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.RutinaRepository

/**
 * Caso de uso para eliminar una rutina de un usuario específico.
 *
 * Verifica que la rutina exista y pertenezca al usuario antes de proceder a eliminarla.
 * Si la rutina no pertenece al usuario, lanza una excepción de acceso ilegal.
 */
class EliminarRutinaUseCase(private val rutinaRepository: RutinaRepository) {

    /**
     * Elimina la rutina especificada si pertenece al usuario dado.
     *
     * @param rutinaId ID de la rutina a eliminar.
     * @param userId ID del usuario que intenta eliminar la rutina.
     * @throws IllegalAccessException si la rutina no pertenece al usuario.
     */
    suspend operator fun invoke(rutinaId: Int, userId: Int) {
        val rutina = rutinaRepository.obtenerRutinaPorId(rutinaId)
        if (rutina != null && rutina.userId == userId) {
            rutinaRepository.eliminarRutina(rutina)
        } else {
            throw IllegalAccessException("No puedes eliminar una rutina que no te pertenece.")
        }
    }
}