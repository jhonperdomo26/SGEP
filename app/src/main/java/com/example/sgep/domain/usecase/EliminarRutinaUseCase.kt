package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.RutinaRepository

class EliminarRutinaUseCase(private val rutinaRepository: RutinaRepository) {
    suspend operator fun invoke(rutinaId: Int, userId: Int) {
        val rutina = rutinaRepository.obtenerRutinaPorId(rutinaId)
        if (rutina != null && rutina.userId == userId) {
            rutinaRepository.eliminarRutina(rutina)
        } else {
            throw IllegalAccessException("No puedes eliminar una rutina que no te pertenece.")
        }
    }
}