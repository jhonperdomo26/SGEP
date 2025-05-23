package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * SerieEjercicioEntity almacena los datos de cada serie configurada para un ejercicio en una rutina.
 * Aquí el usuario pone cuántas series, peso, reps, descanso por serie.
 */
@Entity(
    tableName = "serie_ejercicio",
    foreignKeys = [
        ForeignKey(
            entity = EjercicioEnRutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["ejercicioEnRutinaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SerieEjercicioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ejercicioEnRutinaId: Int, // A qué ejercicio en la rutina pertenece
    val numeroSerie: Int,         // Serie 1, 2, 3, etc.
    val peso: Float,              // Peso asignado (kg)
    val repeticiones: Int,        // Reps asignadas
    val descanso: Int             // Descanso entre series (segundos)
)