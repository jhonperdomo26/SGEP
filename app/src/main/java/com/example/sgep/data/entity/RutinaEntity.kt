package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una rutina de entrenamiento creada por un usuario en el sistema.
 *
 * Esta entidad almacena los metadatos principales de una rutina, mientras que los ejercicios
 * que la componen se gestionan en entidades relacionadas mediante el campo [id].
 *
 * @property id Identificador único autogenerado (clave primaria). Valor por defecto: 0.
 * @property nombre Nombre descriptivo de la rutina (ej: "Rutina volumen piernas").
 * @property fechaCreacion Fecha de creación en milisegundos desde la época Unix.
 *                       Valor por defecto: momento de creación del objeto ([System.currentTimeMillis]).
 * @property userId Identificador del usuario creador de la rutina. Debe corresponder a un [UserEntity.id] existente.
 *
 * @see EjercicioEnRutinaEntity Para los ejercicios que componen esta rutina.
 * @see UserEntity Para la entidad de usuario asociada.
 */
@Entity(tableName = "rutina")
data class RutinaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val userId: Int
)