package com.example.sgep.data.repository

import com.example.sgep.data.dao.MedidaCorporalDao
import com.example.sgep.data.dao.errors.MedidasError
import com.example.sgep.data.entity.MedidaCorporalEntity
import kotlin.math.abs

/**
 * Repositorio encargado de manejar las operaciones relacionadas con
 * las medidas corporales del usuario, incluyendo validación, creación,
 * consulta, actualización y eliminación.
 */
class MedidaCorporalRepository(
    private val medidaCorporalDao: MedidaCorporalDao
) {

    /**
     * Registra una nueva medida corporal para un usuario dado.
     *
     * @param usuarioId ID del usuario que registra la medida.
     * @param medida Objeto MedidaCorporalEntity con los datos a registrar.
     * @return Result<Long> con el ID de la medida creada o error.
     */
    suspend fun registrarMedida(usuarioId: Int, medida: MedidaCorporalEntity): Result<Long> {
        return try {
            // Validar que la medida corresponda al usuario
            if (medida.userId != usuarioId) throw MedidasError.UsuarioNoCoincide()

            // Validar valores y proporciones de la medida corporal
            validarMedidasCorporales(medida)

            // Insertar la medida en la base de datos
            Result.success(medidaCorporalDao.insertMedida(medida))
        } catch (e: MedidasError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(MedidasError.DatabaseError(e))
        }
    }

    /**
     * Valida las medidas corporales para asegurar valores positivos,
     * rangos realistas, proporciones correctas y simetría corporal.
     *
     * @param medida MedidaCorporalEntity con los valores a validar.
     * @throws MedidasError si alguna validación falla.
     */
    fun validarMedidasCorporales(medida: MedidaCorporalEntity) {
        with(medida) {
            // Validar que todos los valores sean positivos
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

            // Validar rangos considerados realistas para adultos
            validarRango("Peso", peso, 30f..300f)
            validarRango("Cuello", cuello, 20f..52f)
            validarRango("Hombros", hombros, 80f..180f)
            validarRango("Pecho", pecho, 60f..150f)
            validarRango("Cintura", cintura, 50f..150f)
            validarRango("Cadera", cadera, 70f..150f)
            validarRango("Glúteos", gluteos, 70f..150f)
            validarRango("Muslo", musloIzq, 35f..90f)
            validarRango("Muslo", musloDer, 35f..90f)
            validarRango("Gemelo", gemeloIzq, 25f..55f)
            validarRango("Gemelo", gemeloDer, 25f..55f)
            validarRango("Bíceps", bicepsIzq, 20f..60f)
            validarRango("Bíceps", bicepsDer, 20f..60f)
            validarRango("Antebrazo", antebrazoIzq, 15f..50f)
            validarRango("Antebrazo", antebrazoDer, 15f..50f)

            // Validación de proporciones corporales lógicas
            if (cintura >= pecho) {
                throw MedidasError.ProporcionInvalida("La cintura no puede ser mayor que el pecho")
            }

            if (cadera >= 1.5f * pecho) {
                throw MedidasError.ProporcionInvalida("Proporción cadera/pecho no realista")
            }

            // Validación de simetría, permitiendo hasta 15% de diferencia entre lados
            validarSimetria("Muslo", musloIzq, musloDer)
            validarSimetria("Gemelo", gemeloIzq, gemeloDer)
            validarSimetria("Bíceps", bicepsIzq, bicepsDer)
            validarSimetria("Antebrazo", antebrazoIzq, antebrazoDer)
        }
    }

    /**
     * Valida que un valor esté dentro de un rango cerrado dado.
     *
     * @param nombreCampo Nombre descriptivo del campo.
     * @param valor Valor numérico a validar.
     * @param rango Rango válido para el valor.
     * @throws MedidasError.ValorFueraDeRango si el valor no está en el rango.
     */
    private fun validarRango(nombreCampo: String, valor: Float, rango: ClosedRange<Float>) {
        if (valor !in rango) throw MedidasError.ValorFueraDeRango(
            nombreCampo,
            "${rango.start} - ${rango.endInclusive}"
        )
    }

    /**
     * Valida la simetría entre lados izquierdo y derecho de una medida corporal.
     *
     * @param nombreCampo Nombre descriptivo del campo.
     * @param ladoIzq Valor del lado izquierdo.
     * @param ladoDer Valor del lado derecho.
     * @throws MedidasError.AsimetriaExcesiva si la diferencia es mayor al 15%.
     */
    private fun validarSimetria(nombreCampo: String, ladoIzq: Float, ladoDer: Float) {
        val diferencia = abs(ladoIzq - ladoDer)
        val maxDiferencia = maxOf(ladoIzq, ladoDer) * 0.15f // 15% de tolerancia

        if (diferencia > maxDiferencia) {
            throw MedidasError.AsimetriaExcesiva(nombreCampo)
        }
    }

    /**
     * Obtiene todas las medidas corporales registradas para un usuario.
     *
     * @param userId ID del usuario.
     * @return Lista de medidas corporales.
     */
    suspend fun obtenerMedidasPorUsuario(userId: Int): List<MedidaCorporalEntity> =
        medidaCorporalDao.getMedidasByUser(userId)

    /**
     * Obtiene una medida corporal específica por su ID.
     *
     * @param medidaId ID de la medida.
     * @return MedidaCorporalEntity o null si no existe.
     */
    suspend fun obtenerMedidaPorId(medidaId: Int): MedidaCorporalEntity? =
        medidaCorporalDao.getMedidaById(medidaId)

    /**
     * Actualiza una medida corporal existente.
     *
     * @param medida MedidaCorporalEntity con datos actualizados.
     */
    suspend fun actualizarMedida(medida: MedidaCorporalEntity) =
        medidaCorporalDao.updateMedida(medida)

    /**
     * Elimina un registro de medida corporal.
     *
     * @param medida MedidaCorporalEntity a eliminar.
     */
    suspend fun eliminarMedida(medida: MedidaCorporalEntity) =
        medidaCorporalDao.deleteMedida(medida)
}