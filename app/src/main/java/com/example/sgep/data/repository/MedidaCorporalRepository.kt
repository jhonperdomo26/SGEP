package com.example.sgep.data.repository

import com.example.sgep.data.dao.MedidaCorporalDao
import com.example.sgep.data.dao.errors.MedidasError
import com.example.sgep.data.entity.MedidaCorporalEntity
import kotlin.math.abs

/**
 * MedidaCorporalRepository maneja los registros de medidas físicas del usuario.
 */
class MedidaCorporalRepository(
    private val medidaCorporalDao: MedidaCorporalDao
) {

    // Insertar una nueva medida corporal
    suspend fun registrarMedida(usuarioId: Int, medida: MedidaCorporalEntity): Result<Long> {
        return try {
            // Validar ownership
            if (medida.userId != usuarioId) throw MedidasError.UsuarioNoCoincide()

            // Validar medidas físicas
            validarMedidasCorporales(medida)

            // Insertar
            Result.success(medidaCorporalDao.insertMedida(medida))
        } catch (e: MedidasError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(MedidasError.DatabaseError(e))
        }
    }

    fun validarMedidasCorporales(medida: MedidaCorporalEntity) {
        with(medida) {
            // Validación básica: valores positivos
            listOf(
                peso to "peso (kg)",
                cuello to "cuello (cm)",
                hombros to "hombros (cm)",
                pecho to "pecho (cm)",
                cintura to "cintura (cm)",
                cadera to "cadera (cm)",
                gluteos to "glúteos (cm)",
                musloIzq to "muslo izquierdo (cm)",
                musloDer to "muslo derecho (cm)",
                gemeloIzq to "gemelo izquierdo (cm)",
                gemeloDer to "gemelo derecho (cm)",
                bicepsIzq to "bíceps izquierdo (cm)",
                bicepsDer to "bíceps derecho (cm)",
                antebrazoIzq to "antebrazo izquierdo (cm)",
                antebrazoDer to "antebrazo derecho (cm)"
            ).forEach { (valor, nombre) ->
                if (valor <= 0f) throw MedidasError.ValorNegativo(nombre)
            }

            // Validación de rangos realistas
            validarRango("Peso", peso, 30f..300f) // kg
            validarRango("Cuello", cuello, 20f..52f) // cm
            validarRango("Pecho", pecho, 60f..150f) // cm
            validarRango("Cintura", cintura, 50f..150f) // cm

            // Validación de simetría (máx. 10% de diferencia)
            validarSimetria("Muslo", musloIzq, musloDer)
            validarSimetria("Gemelo", gemeloIzq, gemeloDer)
            validarSimetria("Bíceps", bicepsIzq, bicepsDer)
            validarSimetria("Antebrazo", antebrazoIzq, antebrazoDer)
        }
    }

    private fun validarRango(nombreCampo: String, valor: Float, rango: ClosedRange<Float>) {
        if (valor !in rango) throw MedidasError.ValorFueraDeRango(
            nombreCampo,
            "${rango.start} - ${rango.endInclusive}"
        )
    }

    private fun validarSimetria(nombreCampo: String, ladoIzq: Float, ladoDer: Float) {
        val diferencia = abs(ladoIzq - ladoDer)
        val maxDiferencia = maxOf(ladoIzq, ladoDer) * 0.1f // 10% de tolerancia

        if (diferencia > maxDiferencia) {
            throw MedidasError.AsimetriaExcesiva(nombreCampo)
        }
    }

    // Obtener todas las medidas de un usuario
    suspend fun obtenerMedidasPorUsuario(userId: Int): List<MedidaCorporalEntity> =
        medidaCorporalDao.getMedidasByUser(userId)

    // Obtener una medida específica por ID
    suspend fun obtenerMedidaPorId(medidaId: Int): MedidaCorporalEntity? =
        medidaCorporalDao.getMedidaById(medidaId)

    // Actualizar una medida existente
    suspend fun actualizarMedida(medida: MedidaCorporalEntity) =
        medidaCorporalDao.updateMedida(medida)

    // Eliminar un registro de medida
    suspend fun eliminarMedida(medida: MedidaCorporalEntity) =
        medidaCorporalDao.deleteMedida(medida)
}