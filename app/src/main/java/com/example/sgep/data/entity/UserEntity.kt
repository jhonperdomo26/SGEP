package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Identificador único
    val nombre: String = "", // Nombre completo, predeterminado vacío
    val email: String = "", // Correo electrónico, predeterminado vacío
    val contraseñaHash: String = "", // Contraseña hasheada, predeterminado vacío
    val pesoActual: Double? = null, // Peso corporal actual (opcional)
    val estatura: Double? = null, // Estatura en metros o cm (opcional)
    val objetivo: String = "" // Objetivo personal, predeterminado vacío
) : Serializable {

    /**
     * Registra el peso actual del usuario.
     *
     * @param peso El peso actual del usuario.
     */
    fun registrarPeso(peso: Float): String {
        return "Peso registrado: $peso kg"
    }

    /**
     * Actualiza las medidas corporales del usuario.
     *
     * @param medidas Mapa de medidas corporales (nombre, valor).
     */
    fun actualizarMedidas(medidas: Map<String, Float>): String {
        return "Medidas actualizadas: $medidas"
    }

}