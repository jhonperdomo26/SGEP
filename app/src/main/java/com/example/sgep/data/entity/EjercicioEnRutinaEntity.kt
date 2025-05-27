package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Establece una relación entre rutinas y ejercicios, permitiendo la configuración personalizada
 * de ejercicios dentro de una rutina específica.
 *
 * Esta entidad funciona como tabla de unión (join table) entre [RutinaEntity] y [EjercicioPredefinidoEntity],
 * permitiendo que una rutina contenga múltiples ejercicios con configuraciones individuales.
 * Las series específicas para cada ejercicio se gestionan en [SerieEjercicioEntity].
 *
 * @property id Identificador único autogenerado.
 * @property rutinaId Referencia a la rutina contenedora (clave foránea a [RutinaEntity]).
 * @property ejercicioPredefinidoId Referencia al ejercicio base (clave foránea implícita a [EjercicioPredefinidoEntity]).
 *
 * @see RutinaEntity Para la entidad de rutina asociada.
 * @see EjercicioPredefinidoEntity Para los ejercicios disponibles.
 * @see SerieEjercicioEntity Para la configuración de series por ejercicio.
 */
@Entity(
    tableName = "ejercicios_en_rutina",
    foreignKeys = [ForeignKey(
        entity = RutinaEntity::class,
        parentColumns = ["id"],
        childColumns = ["rutinaId"],
        onDelete = ForeignKey.CASCADE // Elimina automáticamente los ejercicios si se borra la rutina
    )],
    indices = [Index(value = ["rutinaId"])] // Mejora el rendimiento en búsquedas por rutina
)
data class EjercicioEnRutinaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val rutinaId: Int,
    val ejercicioPredefinidoId: Int
)