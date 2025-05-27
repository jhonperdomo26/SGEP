package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.entity.*
import com.example.sgep.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RutinaViewModel(
    private val crearRutinaUseCase: CrearRutinaUseCase,
    private val obtenerRutinasUseCase: ObtenerRutinasUseCase,
    private val agregarEjercicioARutinaUseCase: AgregarEjercicioARutinaUseCase,
    private val iniciarSesionRutinaUseCase: IniciarSesionRutinaUseCase,
    private val registrarSerieSesionUseCase: RegistrarSerieSesionUseCase,
    private val eliminarRutinaUseCase: EliminarRutinaUseCase,
    private val obtenerEstadisticasPorEjercicioUseCase: ObtenerEstadisticasPorEjercicioUseCase,
) : ViewModel() {

    private val _rutinas = MutableStateFlow<List<RutinaEntity>>(emptyList())
    val rutinas: StateFlow<List<RutinaEntity>> get() = _rutinas

    private val _ejerciciosEnRutina = MutableStateFlow<List<EjercicioEnRutinaEntity>>(emptyList())
    val ejerciciosEnRutina: StateFlow<List<EjercicioEnRutinaEntity>> get() = _ejerciciosEnRutina

    private val _ejerciciosPredefinidos = MutableStateFlow<List<EjercicioPredefinidoEntity>>(emptyList())
    val ejerciciosPredefinidos: StateFlow<List<EjercicioPredefinidoEntity>> get() = _ejerciciosPredefinidos

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> get() = _mensaje

    private val _seriesPorEjercicio = MutableStateFlow<List<RegistroSerieSesionEntity>>(emptyList())
    val seriesPorEjercicio: StateFlow<List<RegistroSerieSesionEntity>> get() = _seriesPorEjercicio

    fun cargarRutinas(userId: Int) {
        viewModelScope.launch {
            try {
                _rutinas.value = obtenerRutinasUseCase(userId)
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar rutinas: ${e.message}"
            }
        }
    }

    fun crearRutina(nombre: String, userId: Int) {
        viewModelScope.launch {
            try {
                crearRutinaUseCase(nombre, userId)
                cargarRutinas(userId)
                _mensaje.value = "Rutina creada"
            } catch (e: Exception) {
                _mensaje.value = "Error al crear rutina: ${e.message}"
            }
        }
    }

    fun cargarEjerciciosEnRutina(rutinaId: Int) {
        viewModelScope.launch {
            try {
                _ejerciciosEnRutina.value = obtenerRutinasUseCase.obtenerEjerciciosDeRutina(rutinaId)
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar ejercicios: ${e.message}"
            }
        }
    }

    fun cargarEjerciciosPredefinidos() {
        viewModelScope.launch {
            try {
                _ejerciciosPredefinidos.value = obtenerRutinasUseCase.obtenerEjerciciosPredefinidos()
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar ejercicios predefinidos: ${e.message}"
            }
        }
    }

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
                cargarEjerciciosEnRutina(rutinaId)
                _mensaje.value = "Ejercicio agregado"
            } catch (e: Exception) {
                _mensaje.value = "Error al agregar ejercicio: ${e.message}"
            }
        }
    }

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

    fun eliminarRutina(rutina: RutinaEntity, userId: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                eliminarRutinaUseCase(rutina.id, userId)
                cargarRutinas(userId)
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

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}

