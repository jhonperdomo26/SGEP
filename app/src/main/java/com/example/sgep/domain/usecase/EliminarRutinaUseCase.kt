package com.example.sgep.domain.usecase

import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.data.repository.RutinaRepository

class EliminarRutinaUseCase(private val rutinaRepository: RutinaRepository) {
    suspend operator fun invoke(rutina: RutinaEntity) {
        rutinaRepository.eliminarRutina(rutina)
    }
}
