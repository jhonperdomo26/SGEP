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
    val objetivo: String = "" // Objetivo personal, predeterminado vacío
) : Serializable