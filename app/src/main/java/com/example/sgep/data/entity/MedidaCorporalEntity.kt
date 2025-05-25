package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * MedidaCorporalEntity representa un registro de medidas físicas del usuario en una fecha determinada.
 */
@Entity(
    tableName = "medida_corporal",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedidaCorporalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Referencia al usuario propietario de las medidas

    val fecha: Long = System.currentTimeMillis(), // Fecha de registro

    val peso: Float,
    val cuello: Float,
    val hombros: Float,
    val pecho: Float,
    val cintura: Float,
    val cadera: Float,
    val gluteos: Float,
    val musloIzq: Float,
    val musloDer: Float,
    val gemeloIzq: Float,
    val gemeloDer: Float,
    val bicepsIzq: Float,
    val bicepsDer: Float,
    val antebrazoIzq: Float,
    val antebrazoDer: Float
)