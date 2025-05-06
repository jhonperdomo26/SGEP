package com.example.sgep.data.entity

data class Cronometro(
    var duracionDescanso: Int = 0, // Duración del descanso en segundos
    var enPausa: Boolean = false // Estado del cronómetro
) {

    /**
     * Inicia el cronómetro.
     */
    fun iniciar() {
        enPausa = false
        println("Cronómetro iniciado.")
    }

    /**
     * Pausa el cronómetro.
     */
    fun pausar() {
        enPausa = true
        println("Cronómetro en pausa.")
    }

    /**
     * Reinicia el cronómetro.
     */
    fun reiniciar() {
        enPausa = false
        println("Cronómetro reiniciado.")
    }
}