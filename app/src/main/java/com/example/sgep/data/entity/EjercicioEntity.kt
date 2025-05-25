package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "exercises")
data class EjercicioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Identificador único del ejercicio
    val nombre: String = "", // Nombre del ejercicio
    val grupoMuscular: String = "", // Grupo muscular involucrado
    val tipo: String = "", // Tipo de ejercicio (pesas libres, máquinas, etc.)
    val posicion: String = "" // Posición del ejercicio (de pie, sentado, etc.)
) : Serializable {

    /**
     * Calcula el 1 Repetición Máxima (1RM) usando peso y repeticiones.
     *
     * @param peso Peso levantado en kg.
     * @param repeticiones Número de repeticiones realizadas.
     */
    fun calcularRM(peso: Float, repeticiones: Int): Float {
        if (repeticiones <= 0) throw IllegalArgumentException("Repeticiones deben ser mayores a 0")
        return peso / (1.0278f - 0.0278f * repeticiones)
    }
}