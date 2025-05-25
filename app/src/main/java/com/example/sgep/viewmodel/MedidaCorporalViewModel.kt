package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.entity.MedidaCorporalEntity
import com.example.sgep.domain.usecase.MedidaCorporalUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado y las operaciones relacionadas con medidas corporales.
 * Recibe el caso de uso necesario (inyectado por constructor o factory).
 */
class MedidaCorporalViewModel(
    private val useCase: MedidaCorporalUseCase
) : ViewModel() {

    // --- Estado para la lista de medidas ---
    private val _medidas = MutableStateFlow<List<MedidaCorporalEntity>>(emptyList())
    val medidas: StateFlow<List<MedidaCorporalEntity>> get() = _medidas

    // --- Estado para la medida seleccionada ---
    private val _medidaSeleccionada = MutableStateFlow<MedidaCorporalEntity?>(null)
    val medidaSeleccionada: StateFlow<MedidaCorporalEntity?> get() = _medidaSeleccionada

    // --- Estado para mensajes/errores ---
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> get() = _mensaje

    // --- Estado para diferencias entre medidas ---
    private val _diferencias = MutableStateFlow<Map<String, Float>?>(null)
    val diferencias: StateFlow<Map<String, Float>?> get() = _diferencias

    // --- Cargar todas las medidas de un usuario ---
    fun cargarMedidas(userId: Int) {
        viewModelScope.launch {
            try {
                _medidas.value = useCase.obtenerMedidasPorUsuario(userId)
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar medidas: ${e.message}"
            }
        }
    }

    // --- Registrar una nueva medida ---
    fun registrarMedida(userId: Int, medida: MedidaCorporalEntity) {
        viewModelScope.launch {
            try {
                val resultado = useCase.registrarMedida(userId, medida)
                resultado.onSuccess {
                    _mensaje.value = "Medida registrada correctamente"
                    cargarMedidas(userId) // Actualizar la lista
                }.onFailure { error ->
                    _mensaje.value = "Error al registrar medida: ${error.message}"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error inesperado: ${e.message}"
            }
        }
    }

    // --- Seleccionar una medida específica ---
    fun seleccionarMedida(medidaId: Int) {
        viewModelScope.launch {
            try {
                _medidaSeleccionada.value = useCase.obtenerMedidaPorId(medidaId)
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar medida: ${e.message}"
            }
        }
    }

    // --- Actualizar una medida existente ---
    fun actualizarMedida(medida: MedidaCorporalEntity) {
        viewModelScope.launch {
            try {
                useCase.actualizarMedida(medida)
                _mensaje.value = "Medida actualizada correctamente"
                cargarMedidas(medida.userId)
            } catch (e: Exception) {
                _mensaje.value = "Error al actualizar medida: ${e.message}"
            }
        }
    }

    // --- Eliminar una medida ---
    fun eliminarMedida(medida: MedidaCorporalEntity) {
        viewModelScope.launch {
            try {
                useCase.eliminarMedida(medida)
                _mensaje.value = "Medida eliminada correctamente"
                medida.userId?.let { cargarMedidas(it) } // Actualizar lista si tenemos userId
            } catch (e: Exception) {
                _mensaje.value = "Error al eliminar medida: ${e.message}"
            }
        }
    }

    // --- Obtener la última medida registrada ---
    fun obtenerUltimaMedida(userId: Int) {
        viewModelScope.launch {
            try {
                _medidaSeleccionada.value = useCase.obtenerUltimaMedida(userId)
            } catch (e: Exception) {
                _mensaje.value = "Error al obtener última medida: ${e.message}"
            }
        }
    }

    // --- Calcular diferencias entre dos medidas ---
    fun calcularDiferencias(medidaActual: MedidaCorporalEntity, medidaAnterior: MedidaCorporalEntity) {
        try {
            _diferencias.value = useCase.calcularDiferencias(medidaActual, medidaAnterior)
        } catch (e: Exception) {
            _mensaje.value = "Error al calcular diferencias: ${e.message}"
        }
    }

    // --- Limpiar mensajes ---
    fun limpiarMensaje() {
        _mensaje.value = null
    }

    // --- Limpiar medida seleccionada ---
    fun limpiarMedidaSeleccionada() {
        _medidaSeleccionada.value = null
    }

    // --- Limpiar diferencias ---
    fun limpiarDiferencias() {
        _diferencias.value = null
    }
}