package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * RutinaEntity representa una rutina creada por el usuario.
 * Guarda el nombre, la fecha de creación y el ID del usuario que la creó.
 * Los ejercicios que componen la rutina se guardan aparte, ligados por rutinaId.
 */
@Entity(tableName = "rutina")
data class RutinaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,          // ID único de la rutina
    val nombre: String,                                        // Nombre de la rutina
    val fechaCreacion: Long = System.currentTimeMillis(),     // Fecha de creación en milisegundos
    val userId: Int                                            // ID del usuario creador de la rutina
)
