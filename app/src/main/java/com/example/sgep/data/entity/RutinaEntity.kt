package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * RutinaEntity representa una rutina creada por el usuario.
 * Solo guarda el nombre y la fecha de creación.
 * Los ejercicios que componen la rutina se guardan aparte, ligados por rutinaId.
 */
@Entity(tableName = "rutina")
data class RutinaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID único
    val nombre: String,                                // Nombre de la rutina
    val fechaCreacion: Long = System.currentTimeMillis() // Fecha de creación en milisegundos
)