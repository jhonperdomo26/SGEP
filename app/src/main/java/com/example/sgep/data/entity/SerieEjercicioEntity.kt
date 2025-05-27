package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Representa una serie específica de un ejercicio dentro de una rutina de entrenamiento.
 *
 * Almacena la configuración de peso, repeticiones y descanso para cada serie individual
 * que compone un ejercicio en una rutina. Está vinculada a [EjercicioEnRutinaEntity] mediante
 * una relación de clave foránea con eliminación en cascada.
 *
 * @property id Identificador único autogenerado (clave primaria).
 * @property ejercicioEnRutinaId Clave foránea que referencia al ejercicio asociado en [EjercicioEnRutinaEntity].
 * @property numeroSerie Número ordinal de la serie (ej: 1, 2, 3...) dentro del ejercicio.
 * @property peso Peso utilizado en la serie, en kilogramos. Valor puede ser decimal (ej: 12.5 kg).
 * @property repeticiones Número de repeticiones objetivo para la serie.
 * @property descanso Tiempo de descanso después de esta serie, en segundos.
 *
 * @see EjercicioEnRutinaEntity Para la entidad padre que contiene la configuración base del ejercicio.
 */
@Entity(
    tableName = "serie_ejercicio",
    foreignKeys = [
        ForeignKey(
            entity = EjercicioEnRutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["ejercicioEnRutinaId"],
            onDelete = ForeignKey.CASCADE // Elimina todas las series si se borra el ejercicio padre
        )
    ]
)
data class SerieEjercicioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ejercicioEnRutinaId: Int,
    val numeroSerie: Int,
    val peso: Float,
    val repeticiones: Int,
    val descanso: Int
)