package com.example.sgep.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Registra las medidas corporales de un usuario en un momento específico.
 *
 * Esta entidad permite el seguimiento de la evolución física del usuario,
 * almacenando medidas antropométricas con precisión decimal (en cm/kg).
 * Está vinculada al [UserEntity] mediante una relación de clave foránea.
 *
 * @property id Identificador único autogenerado.
 * @property userId Referencia al usuario dueño del registro (clave foránea a [UserEntity]).
 * @property fecha Fecha de registro en milisegundos desde epoch. Valor por defecto: momento de creación.
 * @property peso Peso corporal en kilogramos.
 * @property cuello Circunferencia del cuello en centímetros.
 * @property hombros Circunferencia de hombros en centímetros.
 * @property pecho Circunferencia del pecho en centímetros.
 * @property cintura Circunferencia de cintura en centímetros.
 * @property cadera Circunferencia de cadera en centímetros.
 * @property gluteos Circunferencia de glúteos en centímetros.
 * @property musloIzq Circunferencia del muslo izquierdo en centímetros.
 * @property musloDer Circunferencia del muslo derecho en centímetros.
 * @property gemeloIzq Circunferencia del gemelo izquierdo en centímetros.
 * @property gemeloDer Circunferencia del gemelo derecho en centímetros.
 * @property bicepsIzq Circunferencia del bíceps izquierdo en centímetros.
 * @property bicepsDer Circunferencia del bíceps derecho en centímetros.
 * @property antebrazoIzq Circunferencia del antebrazo izquierdo en centímetros.
 * @property antebrazoDer Circunferencia del antebrazo derecho en centímetros.
 *
 * @see UserEntity Para la entidad de usuario asociada.
 */
@Entity(
    tableName = "medida_corporal",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Elimina medidas si se borra el usuario
        )
    ]
)
data class MedidaCorporalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val fecha: Long = System.currentTimeMillis(),
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