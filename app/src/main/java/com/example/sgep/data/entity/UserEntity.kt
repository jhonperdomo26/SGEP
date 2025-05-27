package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Representa un usuario en la base de datos local (Room).
 *
 * Esta clase es una entidad Room que almacena información básica del usuario,
 * incluyendo credenciales de acceso (email y contraseña hasheada) y su objetivo personal.
 * Implementa [Serializable] para permitir su paso entre Activities/Fragments.
 *
 * @property id Identificador único autogenerado por Room. Valor por defecto: 0 (la BD lo reemplazará).
 * @property nombre Nombre completo del usuario. Valor por defecto: cadena vacía.
 * @property email Correo electrónico único del usuario (debe ser válido). Valor por defecto: cadena vacía.
 * @property contraseñaHash Contraseña hasheada (usando algoritmos como BCrypt). Valor por defecto: cadena vacía.
 * @property objetivo Descripción del objetivo personal del usuario (ej: "Perder 5 kg"). Valor por defecto: cadena vacía.
 *
 * @see [Serializable]
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String = "",
    val email: String = "",
    val contraseñaHash: String = "",
    val objetivo: String = ""
) : Serializable