package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.sgep.data.converters.EjercicioListConverter
import java.io.Serializable
import java.util.Date

@Entity(tableName = "routines")
data class RutinaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Identificador único de la rutina
    val nombre: String = "", // Nombre de la rutina
    val fechaCreacion: Date = Date(), // Fecha de creación de la rutina
    @TypeConverters(EjercicioListConverter::class) // Conversor para la lista de ejercicios
    val ejercicios: MutableList<EjercicioEntity> = mutableListOf() // Lista de ejercicios
) : Serializable {

    /**
     * Agrega un ejercicio a la rutina.
     *
     * @param ejercicio Ejercicio que se desea agregar.
     */
    fun agregarEjercicio(ejercicio: EjercicioEntity) {
        ejercicios.add(ejercicio)
    }

    /**
     * Elimina un ejercicio de la rutina.
     *
     * @param ejercicio Ejercicio que se desea eliminar.
     */
    fun eliminarEjercicio(ejercicio: EjercicioEntity): Boolean {
        return ejercicios.remove(ejercicio)
    }

    /**
     * Muestra la lista de ejercicios en la rutina.
     */
    fun mostrarRutina(): String {
        return ejercicios.joinToString(separator = "\n") { it.nombre }
    }
}