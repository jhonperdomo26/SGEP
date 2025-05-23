package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * EjercicioPredefinidoEntity guarda los ejercicios que ya trae la app.
 * El usuario puede elegir entre estos, pero no editarlos.
 * Puedes poblar esta tabla al instalar la app.
 */
@Entity(tableName = "ejercicio_predefinido")
data class EjercicioPredefinidoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,          // Ej: "Press de banca"
    val grupoMuscular: String,   // Ej: "Pecho"
    val descripcion: String      // Breve instrucción o tips
)