package com.example.sgep.domain.usecase

import com.example.sgep.data.entity.MedidaCorporalEntity
import com.example.sgep.data.repository.MedidaCorporalRepository

/**
 * Casos de uso para la gestión de medidas corporales.
 * Encapsula la lógica de negocio relacionada con medidas corporales,
 * y utiliza el repositorio para realizar operaciones de datos.
 */
class MedidaCorporalUseCase(
    private val repository: MedidaCorporalRepository
) {

    /**
     * Registra una nueva medida corporal para un usuario, con las validaciones necesarias.
     *
     * @param usuarioId ID del usuario propietario de la medida.
     * @param medida Objeto con los datos de la medida corporal.
     * @return Result<Long> que contiene el ID de la medida insertada o un error.
     */
    suspend fun registrarMedida(
        usuarioId: Int,
        medida: MedidaCorporalEntity
    ): Result<Long> = repository.registrarMedida(usuarioId, medida)

    /**
     * Obtiene todas las medidas registradas para un usuario, ordenadas por fecha descendente.
     *
     * @param userId ID del usuario.
     * @return Lista de medidas corporales del usuario.
     */
    suspend fun obtenerMedidasPorUsuario(userId: Int): List<MedidaCorporalEntity> =
        repository.obtenerMedidasPorUsuario(userId)

    /**
     * Obtiene una medida corporal específica dado su ID.
     *
     * @param medidaId ID de la medida.
     * @return MedidaCorporalEntity o null si no existe.
     */
    suspend fun obtenerMedidaPorId(medidaId: Int): MedidaCorporalEntity? =
        repository.obtenerMedidaPorId(medidaId)

    /**
     * Actualiza los datos de una medida corporal existente.
     *
     * @param medida Objeto con los datos actualizados de la medida.
     */
    suspend fun actualizarMedida(medida: MedidaCorporalEntity) =
        repository.actualizarMedida(medida)

    /**
     * Elimina un registro de medida corporal.
     *
     * @param medida Medida a eliminar.
     */
    suspend fun eliminarMedida(medida: MedidaCorporalEntity) =
        repository.eliminarMedida(medida)

    /**
     * Obtiene la última medida registrada por un usuario, o null si no hay registros.
     *
     * @param userId ID del usuario.
     * @return Última medida corporal registrada o null.
     */
    suspend fun obtenerUltimaMedida(userId: Int): MedidaCorporalEntity? =
        repository.obtenerMedidasPorUsuario(userId).firstOrNull()

    /**
     * Calcula la diferencia entre dos medidas corporales, campo por campo.
     *
     * @param medidaActual Medida más reciente.
     * @param medidaAnterior Medida de referencia para comparación.
     * @return Mapa con las diferencias por cada campo (clave = nombre del campo, valor = diferencia).
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
}