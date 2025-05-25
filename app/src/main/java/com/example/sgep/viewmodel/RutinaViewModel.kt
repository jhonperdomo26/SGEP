package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.entity.*
import com.example.sgep.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * RutinaViewModel administra toda la lógica y el estado relacionado con las rutinas y sus ejercicios.
 * Recibe los casos de uso necesarios (inyectados por constructor o factory).
 */
class RutinaViewModel(
    private val crearRutinaUseCase: CrearRutinaUseCase,
    private val obtenerRutinasUseCase: ObtenerRutinasUseCase,
    private val agregarEjercicioARutinaUseCase: AgregarEjercicioARutinaUseCase,
    private val iniciarSesionRutinaUseCase: IniciarSesionRutinaUseCase,
    private val registrarSerieSesionUseCase: RegistrarSerieSesionUseCase,
    private val eliminarRutinaUseCase: EliminarRutinaUseCase,
    private val obtenerEstadisticasPorEjercicioUseCase: ObtenerEstadisticasPorEjercicioUseCase,
) : ViewModel() {
    // --- Rutinas principales ---
    private val _rutinas = MutableStateFlow<List<RutinaEntity>>(emptyList())
    val rutinas: StateFlow<List<RutinaEntity>> get() = _rutinas

    // --- NUEVO: Estado para los ejercicios en la rutina seleccionada ---
    private val _ejerciciosEnRutina = MutableStateFlow<List<EjercicioEnRutinaEntity>>(emptyList())
    val ejerciciosEnRutina: StateFlow<List<EjercicioEnRutinaEntity>> get() = _ejerciciosEnRutina

    // --- NUEVO: Estado para los ejercicios predefinidos ---
    private val _ejerciciosPredefinidos = MutableStateFlow<List<EjercicioPredefinidoEntity>>(emptyList())
    val ejerciciosPredefinidos: StateFlow<List<EjercicioPredefinidoEntity>> get() = _ejerciciosPredefinidos

    // --- Estado para mensajes/errores (opcional) ---
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> get() = _mensaje

    private val _seriesPorEjercicio = MutableStateFlow<List<RegistroSerieSesionEntity>>(emptyList())
    val seriesPorEjercicio: StateFlow<List<RegistroSerieSesionEntity>> get() = _seriesPorEjercicio

    // --- Cargar todas las rutinas disponibles ---
    fun cargarRutinas() {
        viewModelScope.launch {
            try {
                _rutinas.value = obtenerRutinasUseCase()
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar rutinas: ${e.message}"
            }
        }
    }

    // --- Crear una nueva rutina ---
    fun crearRutina(nombre: String) {
        viewModelScope.launch {
            try {
                crearRutinaUseCase(nombre)
                cargarRutinas() // Actualiza la lista
                _mensaje.value = "Rutina creada"
            } catch (e: Exception) {
                _mensaje.value = "Error al crear rutina: ${e.message}"
            }
        }
    }

    // --- NUEVO: Cargar ejercicios asociados a una rutina ---
    fun cargarEjerciciosEnRutina(rutinaId: Int) {
        viewModelScope.launch {
            try {
                // Asegúrate de tener este método en tu UseCase/Repositorio
                _ejerciciosEnRutina.value = obtenerRutinasUseCase.obtenerEjerciciosDeRutina(rutinaId)
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar ejercicios: ${e.message}"
            }
        }
    }

    // --- NUEVO: Cargar la lista de ejercicios predefinidos ---
    fun cargarEjerciciosPredefinidos() {
        viewModelScope.launch {
            try {
                // Asegúrate de tener este método en tu UseCase/Repositorio
                _ejerciciosPredefinidos.value = obtenerRutinasUseCase.obtenerEjerciciosPredefinidos()
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar ejercicios predefinidos: ${e.message}"
            }
        }
    }

    // --- NUEVO: Agregar ejercicio a rutina y recargar lista ---
    fun agregarEjercicioARutinaYRecargar(rutinaId: Int, ejercicioPredefinidoId: Int) {
        viewModelScope.launch {
            try {
                agregarEjercicioARutinaUseCase(rutinaId, ejercicioPredefinidoId)
                cargarEjerciciosEnRutina(rutinaId)
                _mensaje.value = "Ejercicio agregado"
            } catch (e: Exception) {
                _mensaje.value = "Error al agregar ejercicio: ${e.message}"
            }
        }
    }

    fun agregarEjercicioARutina(rutinaId: Int, ejercicioPredefinidoId: Int) {
        viewModelScope.launch {
            try {
                agregarEjercicioARutinaUseCase(rutinaId, ejercicioPredefinidoId)
                cargarEjerciciosEnRutina(rutinaId) // Recarga la lista de ejercicios de la rutina
                _mensaje.value = "Ejercicio agregado"
            } catch (e: Exception) {
                _mensaje.value = "Error al agregar ejercicio: ${e.message}"
            }
        }
    }

    // --- Iniciar una nueva sesión de entrenamiento para una rutina ---
    fun iniciarSesion(rutinaId: Int, onSesionCreada: (Long) -> Unit) {
        viewModelScope.launch {
            try {
                val sesionId = iniciarSesionRutinaUseCase(rutinaId)
                _mensaje.value = "¡Sesión iniciada!"
                onSesionCreada(sesionId)
            } catch (e: Exception) {
                _mensaje.value = "Error al iniciar sesión: ${e.message}"
            }
        }
    }

    // --- Registrar una serie completada durante una sesión ---
    fun registrarSerieSesion(
        sesionRutinaId: Int,
        ejercicioEnRutinaId: Int,
        numeroSerie: Int,
        peso: Float,
        repeticiones: Int,
        completada: Boolean
    ) {
        viewModelScope.launch {
            try {
                registrarSerieSesionUseCase(
                    sesionRutinaId, ejercicioEnRutinaId, numeroSerie, peso, repeticiones, completada
                )
                _mensaje.value = "Serie registrada"
            } catch (e: Exception) {
                _mensaje.value = "Error al registrar serie: ${e.message}"
            }
        }
    }

    fun eliminarRutina(rutina: RutinaEntity, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                eliminarRutinaUseCase(rutina)
                cargarRutinas()
                onDone()
            } catch (e: Exception) {
                _mensaje.value = "Error al eliminar rutina: ${e.message}"
            }
        }
    }

    fun cargarSeriesPorEjercicio(ejercicioEnRutinaId: Int) {
        viewModelScope.launch {
            try {
                val series = obtenerEstadisticasPorEjercicioUseCase(ejercicioEnRutinaId)
                _seriesPorEjercicio.value = series
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar series: ${e.message}"
            }
        }
    }

    // --- Limpia el mensaje de estado ---
    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
