package com.example.sgep.data.entity

import java.io.Serializable

data class Estadisticas(
    var volumenEntrenamiento: Float = 0f, // Volumen total de entrenamiento
    var medidasCorporales: MutableMap<String, Float> = mutableMapOf(), // Medidas corporales del usuario
    var progresoEjercicios: MutableList<Float> = mutableListOf() // Progreso de los ejercicios realizados
) : Serializable {

    /**
     * Registra el RIR (Repeticiones en Reserva).
     */
    fun registrarRIR(rir: Int): String {
        return "RIR registrado: $rir"
    }

    /**
     * Registra el RPE (Esfuerzo Percibido).
     */
    fun registrarRPE(rpe: Int): String {
        return "RPE registrado: $rpe"
    }

    /**
     * Genera un reporte de las estadísticas.
     */
    fun generarReporte(): String {
        return "Volumen: $volumenEntrenamiento, Medidas: $medidasCorporales, Progreso: $progresoEjercicios"
    }
}