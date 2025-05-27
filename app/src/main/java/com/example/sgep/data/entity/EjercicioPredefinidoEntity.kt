package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa un ejercicio predefinido en el sistema que los usuarios pueden seleccionar para sus rutinas.
 *
 * Estos ejercicios son inmutables (no editables por usuarios) y pueden ser precargados
 * durante la instalación de la aplicación. Cada ejercicio incluye metadatos para facilitar
 * su búsqueda y selección.
 *
 * @property id Identificador único autogenerado. Valor por defecto: 0.
 * @property nombre Nombre oficial del ejercicio (ej: "Press de banca con barra").
 * @property grupoMuscular Grupo muscular principal que trabaja el ejercicio (ej: "Pecho", "Piernas").
 *                        Debe usar una taxonomía consistente en toda la app.
 * @property descripcion Instrucciones de ejecución o tips de seguridad. Formato: texto plano (sin HTML/Markdown).
 *
 * @see EjercicioEnRutinaEntity Para ver cómo los usuarios incorporan estos ejercicios en sus rutinas.
 */
@Entity(tableName = "ejercicio_predefinido")
data class EjercicioPredefinidoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val grupoMuscular: String,
    val descripcion: String
)