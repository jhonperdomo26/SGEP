package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Representa una sesión de entrenamiento realizada por un usuario en la base de datos.
 *
 * Esta entidad está vinculada a [RutinaEntity] mediante una relación de clave foránea
 * que permite el borrado en cascada. Registra el momento exacto en que se ejecutó una rutina.
 *
 * @property id Identificador único autogenerado (clave primaria).
 * @property rutinaId Referencia a la rutina realizada (clave foránea que relaciona con [RutinaEntity.id]).
 * @property fecha Timestamp de cuando se inició la sesión (en milisegundos desde epoch).
 *                 Valor por defecto: momento de creación del objeto ([System.currentTimeMillis]).
 *
 * @see RutinaEntity Para la entidad de rutina asociada.
 */
@Entity(
    tableName = "sesion_rutina",
    foreignKeys = [
        ForeignKey(
            entity = RutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["rutinaId"],
            onDelete = ForeignKey.CASCADE // Eliminación en cascada automática
        )
    ]
)
data class SesionRutinaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val rutinaId: Int,
    val fecha: Long = System.currentTimeMillis()
)