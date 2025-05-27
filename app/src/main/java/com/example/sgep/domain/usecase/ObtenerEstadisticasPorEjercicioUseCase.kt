package com.example.sgep.domain.usecase

import com.example.sgep.data.entity.RegistroSerieSesionEntity
import com.example.sgep.data.repository.SesionRutinaRepository

/**
 * Caso de uso para obtener las estadísticas de series realizadas
 * en las sesiones de entrenamiento para un ejercicio específico dentro de una rutina.
 *
 * @property sesionRutinaRepository Repositorio para acceder a los datos de sesiones y series.
 */
class ObtenerEstadisticasPorEjercicioUseCase(
    private val sesionRutinaRepository: SesionRutinaRepository
) {
    /**
     * Obtiene todas las series registradas para un ejercicio específico dentro de las sesiones.
     *
     * @param ejercicioEnRutinaId ID del ejercicio en la rutina.
     * @return Lista de registros de series realizadas en sesiones para ese ejercicio.
     */
    suspend operator fun invoke(ejercicioEnRutinaId: Int): List<RegistroSerieSesionEntity> {
        return sesionRutinaRepository.obtenerSeriesPorEjercicio(ejercicioEnRutinaId)
    }
}