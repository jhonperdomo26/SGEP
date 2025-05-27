package com.example.sgep.domain.usecase

import com.example.sgep.data.repository.SesionRutinaRepository

/**
 * Caso de uso que permite registrar una serie realizada por el usuario
 * durante una sesión de entrenamiento.
 *
 * Este caso de uso delega la operación al repositorio correspondiente.
 *
 * @property sesionRutinaRepository Repositorio para manejar datos de sesiones de rutina.
 */
class RegistrarSerieSesionUseCase(private val sesionRutinaRepository: SesionRutinaRepository) {

    /**
     * Ejecuta el registro de una serie en una sesión de rutina.
     *
     * @param sesionRutinaId Identificador de la sesión de rutina.
     * @param ejercicioEnRutinaId Identificador del ejercicio dentro de la rutina.
     * @param numeroSerie Número de la serie realizada.
     * @param peso Peso utilizado en la serie (en kg o lb según contexto).
     * @param repeticiones Número de repeticiones completadas en la serie.
     * @param completada Indica si la serie fue completada satisfactoriamente.
     * @return El ID generado del registro insertado, o una excepción en caso de error.
     */
    suspend operator fun invoke(
        sesionRutinaId: Int,
        ejercicioEnRutinaId: Int,
        numeroSerie: Int,
        peso: Float,
        repeticiones: Int,
        completada: Boolean
    ): Long = sesionRutinaRepository.registrarSerieEnSesion(
        sesionRutinaId,
        ejercicioEnRutinaId,
        numeroSerie,
        peso,
        repeticiones,
        completada
    )
}