package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.dao.errors.MedidasError
import com.example.sgep.data.entity.MedidaCorporalEntity
import com.example.sgep.domain.usecase.MedidaCorporalUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado y las operaciones relacionadas con medidas corporales.
 *
 * Este ViewModel interactúa con el [MedidaCorporalUseCase] para manejar la lógica de negocio,
 * como obtener, registrar y calcular diferencias entre medidas corporales.
 *
 * @property useCase Caso de uso para la gestión de medidas corporales.
 */
class MedidaCorporalViewModel(
    private val useCase: MedidaCorporalUseCase
) : ViewModel() {

    /** Flujo que emite errores específicos relacionados con las medidas corporales. */
    private val _error = MutableStateFlow<MedidasError?>(null)
    val error: StateFlow<MedidasError?> = _error

    /** Flujo que mantiene la lista actual de medidas corporales del usuario. */
    private val _medidas = MutableStateFlow<List<MedidaCorporalEntity>>(emptyList())
    val medidas: StateFlow<List<MedidaCorporalEntity>> get() = _medidas

    /** Flujo que mantiene la medida corporal actualmente seleccionada. */
    private val _medidaSeleccionada = MutableStateFlow<MedidaCorporalEntity?>(null)

    /** Flujo que emite mensajes informativos o de error para la UI. */
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> get() = _mensaje

    /** Flujo que contiene las diferencias calculadas entre dos medidas corporales. */
    private val _diferencias = MutableStateFlow<Map<String, Float>?>(null)
    val diferencias: StateFlow<Map<String, Float>?> get() = _diferencias

    /**
     * Carga todas las medidas corporales asociadas a un usuario dado.
     *
     * @param userId Identificador del usuario.
     */
    fun cargarMedidas(userId: Int) {
        viewModelScope.launch {
            try {
                _medidas.value = useCase.obtenerMedidasPorUsuario(userId)
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar medidas: ${e.message}"
            }
        }
    }

    /**
     * Registra una nueva medida corporal para un usuario.
     *
     * @param userId Identificador del usuario.
     * @param medida Objeto que contiene los datos de la medida corporal a registrar.
     */
    fun registrarMedida(userId: Int, medida: MedidaCorporalEntity) {
        viewModelScope.launch {
            try {
                val resultado = useCase.registrarMedida(userId, medida)
                resultado.onSuccess {
                    _mensaje.value = "Medida registrada correctamente"
                    cargarMedidas(userId) // Actualiza la lista tras registro exitoso
                }.onFailure { error ->
                    _mensaje.value = "Error al registrar medida: ${error.message}"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error inesperado: ${e.message}"
            }
        }
    }

    /**
     * Selecciona una medida corporal por su ID para mostrar o editar.
     *
     * @param medidaId Identificador de la medida corporal a seleccionar.
     */
    fun seleccionarMedida(medidaId: Int) {
        viewModelScope.launch {
            try {
                _medidaSeleccionada.value = useCase.obtenerMedidaPorId(medidaId)
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar medida: ${e.message}"
            }
        }
    }

    /**
     * Calcula las diferencias entre una medida corporal actual y una anterior.
     *
     * @param medidaActual Medida corporal más reciente.
     * @param medidaAnterior Medida corporal previa con la que se compara.
     */
    fun calcularDiferencias(medidaActual: MedidaCorporalEntity, medidaAnterior: MedidaCorporalEntity) {
        try {
            _diferencias.value = useCase.calcularDiferencias(medidaActual, medidaAnterior)
        } catch (e: Exception) {
            _mensaje.value = "Error al calcular diferencias: ${e.message}"
        }
    }

    /** Limpia el mensaje actual, usualmente usado para ocultar notificaciones en la UI. */
    fun limpiarMensaje() {
        _mensaje.value = null
    }

    /** Limpia las diferencias calculadas. */
    fun limpiarDiferencias() {
        _diferencias.value = null
    }

    /** Limpia el error actual. */
    fun limpiarError() {
        _error.value = null
    }

    /**
     * Establece un error específico relacionado con medidas corporales.
     *
     * @param error Error a establecer.
     */
    fun setError(error: MedidasError) {
        _error.value = error
    }
}