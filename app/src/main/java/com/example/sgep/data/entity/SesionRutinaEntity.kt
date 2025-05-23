package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * SesionRutinaEntity guarda cada vez que un usuario inicia una rutina (entrenamiento real).
 * Sirve para guardar historial y progreso.
 */
@Entity(
    tableName = "sesion_rutina",
    foreignKeys = [
        ForeignKey(
            entity = RutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["rutinaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SesionRutinaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rutinaId: Int,                 // Qué rutina se realizó
    val fecha: Long = System.currentTimeMillis() // Fecha y hora de la sesión
)