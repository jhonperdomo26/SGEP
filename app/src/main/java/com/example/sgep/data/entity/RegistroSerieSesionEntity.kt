package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * RegistroSerieSesionEntity almacena el resultado real de cada serie durante una sesión de rutina.
 * Así puedes guardar lo que realmente hizo el usuario (peso, reps, si la terminó, etc.).
 */
@Entity(
    tableName = "registro_serie_sesion",
    foreignKeys = [
        ForeignKey(
            entity = SesionRutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["sesionRutinaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EjercicioEnRutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["ejercicioEnRutinaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RegistroSerieSesionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sesionRutinaId: Int,         // A qué sesión corresponde
    val ejercicioEnRutinaId: Int,    // Qué ejercicio fue (dentro de la rutina)
    val numeroSerie: Int,            // Serie 1, 2, 3...
    val peso: Float,                 // Peso real que usó el usuario
    val repeticiones: Int,           // Reps reales hechas
    val completada: Boolean = false  // Si terminó la serie
)
