package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * EjercicioEnRutinaEntity une una rutina con los ejercicios que contiene.
 * Permite que una misma rutina tenga varios ejercicios y cada uno con configuración propia.
 */
@Entity(
    tableName = "ejercicios_en_rutina",
    foreignKeys = [ForeignKey(
        entity = RutinaEntity::class,
        parentColumns = ["id"],
        childColumns = ["rutinaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["rutinaId"])]
)
data class EjercicioEnRutinaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rutinaId: Int,               // A qué rutina pertenece
    val ejercicioPredefinidoId: Int  // Qué ejercicio es (referencia al predefinido)
)
