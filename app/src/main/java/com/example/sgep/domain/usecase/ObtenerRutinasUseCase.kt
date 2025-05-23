package com.example.sgep.domain.usecase

import com.example.sgep.data.entity.EjercicioEnRutinaEntity
import com.example.sgep.data.entity.EjercicioPredefinidoEntity
import com.example.sgep.data.repository.RutinaRepository

class ObtenerRutinasUseCase(private val rutinaRepository: RutinaRepository) {
    suspend operator fun invoke() = rutinaRepository.obtenerRutinas()

    // Nuevo: retorna los ejercicios de una rutina específica
    suspend fun obtenerEjerciciosDeRutina(rutinaId: Int): List<EjercicioEnRutinaEntity> {
        return rutinaRepository.obtenerEjerciciosDeRutina(rutinaId)
    }

    // Nuevo: retorna la lista de ejercicios predefinidos
    suspend fun obtenerEjerciciosPredefinidos(): List<EjercicioPredefinidoEntity> {
        return rutinaRepository.obtenerEjerciciosPredefinidos()
    }
}
