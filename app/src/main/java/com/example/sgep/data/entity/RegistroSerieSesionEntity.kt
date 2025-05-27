package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Registra el resultado real de una serie ejecutada durante una sesión de entrenamiento.
 *
 * Esta entidad captura la ejecución concreta de cada serie, comparando el rendimiento real
 * con lo planeado en [SerieEjercicioEntity]. Está relacionada con:
 * - [SesionRutinaEntity] (sesión padre)
 * - [EjercicioEnRutinaEntity] (ejercicio específico)
 *
 * @property id Identificador único autogenerado.
 * @property sesionRutinaId Referencia a la sesión donde se registró (clave foránea a [SesionRutinaEntity]).
 * @property ejercicioEnRutinaId Ejercicio concreto realizado (clave foránea a [EjercicioEnRutinaEntity]).
 * @property numeroSerie Número ordinal de la serie (debe coincidir con [SerieEjercicioEntity.numeroSerie]).
 * @property peso Peso realmente utilizado (kg). Puede diferir del peso planeado.
 * @property repeticiones Número de repeticiones realmente completadas.
 * @property completada Indica si la serie se finalizó completamente. Valor por defecto: false.
 *
 * @see SerieEjercicioEntity Para la configuración planeada de la serie.
 * @see SesionRutinaEntity Para el contexto de la sesión de entrenamiento.
 * @see EjercicioEnRutinaEntity Para los detalles del ejercicio en la rutina.
 */
@Entity(
    tableName = "registro_serie_sesion",
    foreignKeys = [
        ForeignKey(
            entity = SesionRutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["sesionRutinaId"],
            onDelete = ForeignKey.CASCADE // Elimina registros si se borra la sesión
        ),
        ForeignKey(
            entity = EjercicioEnRutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["ejercicioEnRutinaId"],
            onDelete = ForeignKey.CASCADE // Elimina registros si se borra el ejercicio
        )
    ]
)
data class RegistroSerieSesionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sesionRutinaId: Int,
    val ejercicioEnRutinaId: Int,
    val numeroSerie: Int,
    val peso: Float,
    val repeticiones: Int,
    val completada: Boolean = false
)