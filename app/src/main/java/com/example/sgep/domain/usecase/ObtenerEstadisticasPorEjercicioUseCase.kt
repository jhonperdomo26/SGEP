package com.example.sgep.domain.usecase

import com.example.sgep.data.entity.RegistroSerieSesionEntity
import com.example.sgep.data.repository.SesionRutinaRepository

class ObtenerEstadisticasPorEjercicioUseCase(
    private val sesionRutinaRepository: SesionRutinaRepository
) {
    suspend operator fun invoke(ejercicioEnRutinaId: Int): List<RegistroSerieSesionEntity> {
        return sesionRutinaRepository.obtenerSeriesPorEjercicio(ejercicioEnRutinaId)
    }
}
