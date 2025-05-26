// domain/usecase/MedidaCorporalUseCase.kt
package com.example.sgep.domain.usecase

import com.example.sgep.data.dao.errors.MedidasError
import com.example.sgep.data.entity.MedidaCorporalEntity
import com.example.sgep.data.repository.MedidaCorporalRepository

/**
 * Casos de uso para la gestión de medidas corporales.
 * Encapsula la lógica de negocio y utiliza el repository para operaciones de datos.
 */
class MedidaCorporalUseCase(
    private val repository: MedidaCorporalRepository
) {

    // region Operaciones CRUD

    /**
     * Registra una nueva medida corporal con validaciones.
     * @param usuarioId ID del usuario dueño de la medida
     * @param medida Datos de la medida corporal
     * @return Result<Long> con el ID de la medida insertada o error
     */
    suspend fun registrarMedida(
        usuarioId: Int,
        medida: MedidaCorporalEntity
    ): Result<Long> = repository.registrarMedida(usuarioId, medida)

    /**
     * Obtiene todas las medidas de un usuario ordenadas por fecha (descendente).
     * @param userId ID del usuario
     * @return List<MedidaCorporalEntity> lista de medidas
     */
    suspend fun obtenerMedidasPorUsuario(userId: Int): List<MedidaCorporalEntity> =
        repository.obtenerMedidasPorUsuario(userId)

    /**
     * Obtiene una medida específica por su ID.
     * @param medidaId ID de la medida
     * @return MedidaCorporalEntity? la medida o null si no existe
     */
    suspend fun obtenerMedidaPorId(medidaId: Int): MedidaCorporalEntity? =
        repository.obtenerMedidaPorId(medidaId)

    /**
     * Actualiza una medida existente.
     * @param medida Medida con los datos actualizados
     */
    suspend fun actualizarMedida(medida: MedidaCorporalEntity) =
        repository.actualizarMedida(medida)

    /**
     * Elimina un registro de medida corporal.
     * @param medida Medida a eliminar
     */
    suspend fun eliminarMedida(medida: MedidaCorporalEntity) =
        repository.eliminarMedida(medida)

    // endregion

    // region Operaciones especializadas

    /**
     * Obtiene la última medida registrada por un usuario.
     * @param userId ID del usuario
     * @return MedidaCorporalEntity? la última medida o null si no hay registros
     */
    suspend fun obtenerUltimaMedida(userId: Int): MedidaCorporalEntity? =
        repository.obtenerMedidasPorUsuario(userId).firstOrNull()

    /**
     * Calcula la diferencia entre dos medidas.
     * @param medidaActual Medida más reciente
     * @param medidaAnterior Medida de referencia
     * @return Map<String, Float> con las diferencias por campo (clave: nombre campo, valor: diferencia)
     */
    fun calcularDiferencias(
        medidaActual: MedidaCorporalEntity,
        medidaAnterior: MedidaCorporalEntity
    ): Map<String, Float> {
        return mapOf(
            "peso" to (medidaActual.peso - medidaAnterior.peso),
            "cuello" to (medidaActual.cuello - medidaAnterior.cuello),
            "hombros" to (medidaActual.hombros - medidaAnterior.hombros),
            "pecho" to (medidaActual.pecho - medidaAnterior.pecho),
            "cintura" to (medidaActual.cintura - medidaAnterior.cintura),
            "cadera" to (medidaActual.cadera - medidaAnterior.cadera),
            "gluteos" to (medidaActual.gluteos - medidaAnterior.gluteos),
            "muslo izq" to (medidaActual.musloIzq - medidaAnterior.musloIzq),
            "muslo der" to (medidaActual.musloDer - medidaAnterior.musloDer),
            "gemelo izq" to (medidaActual.gemeloIzq - medidaAnterior.gemeloIzq),
            "gemelo der" to (medidaActual.gemeloDer - medidaAnterior.gemeloDer),
            "bíceps izq" to (medidaActual.bicepsIzq - medidaAnterior.bicepsIzq),
            "bíceps der" to (medidaActual.bicepsDer - medidaAnterior.bicepsDer),
            "antebrazo izq" to (medidaActual.antebrazoIzq - medidaAnterior.antebrazoIzq),
            "antebrazo der" to (medidaActual.antebrazoDer - medidaAnterior.antebrazoDer)
        )
    }

    // endregion

    // region Validaciones (exponen las del repository si necesarias)

    /**
     * Valida los datos de una medida corporal.
     * @throws MedidasError si alguna validación falla
     */
    fun validarMedidas(medida: MedidaCorporalEntity) {
        // Delega al validador interno del repository
        repository.validarMedidasCorporales(medida)
    }

    // endregion
}