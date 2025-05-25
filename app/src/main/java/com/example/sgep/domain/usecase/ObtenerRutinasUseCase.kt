package com.example.sgep.domain.usecase

import com.example.sgep.data.entity.EjercicioEnRutinaEntity
import com.example.sgep.data.entity.EjercicioPredefinidoEntity
import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.data.repository.RutinaRepository

class ObtenerRutinasUseCase(private val rutinaRepository: RutinaRepository) {
    suspend operator fun invoke() = rutinaRepository.obtenerRutinas()

    // Obtener rutinas por usuario
    suspend operator fun invoke(userId: Int): List<RutinaEntity> {
        return rutinaRepository.obtenerRutinasPorUsuario(userId)
    }

    // Obtener ejercicios de una rutina específica
    suspend fun obtenerEjerciciosDeRutina(rutinaId: Int): List<EjercicioEnRutinaEntity> {
        return rutinaRepository.obtenerEjerciciosDeRutina(rutinaId)
    }

    // Obtener ejercicios predefinidos disponibles
    suspend fun obtenerEjerciciosPredefinidos(): List<EjercicioPredefinidoEntity> {
        return rutinaRepository.obtenerEjerciciosPredefinidos()
    }
}
