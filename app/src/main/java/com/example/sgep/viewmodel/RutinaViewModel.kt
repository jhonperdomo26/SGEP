package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.entity.*
import com.example.sgep.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la gestión de rutinas y sesiones de entrenamiento.
 *
 * Este ViewModel interactúa con los casos de uso que permiten crear, obtener,
 * modificar y eliminar rutinas, además de manejar sesiones y estadísticas.
 *
 * Utiliza [StateFlow] para exponer estados reactivos que pueden ser observados por la UI.
 *
 * @property crearRutinaUseCase Caso de uso para crear una nueva rutina.
 * @property obtenerRutinasUseCase Caso de uso para obtener las rutinas de un usuario.
 * @property agregarEjercicioARutinaUseCase Caso de uso para agregar ejercicios predefinidos a una rutina.
 * @property iniciarSesionRutinaUseCase Caso de uso para iniciar una sesión de rutina.
 * @property registrarSerieSesionUseCase Caso de uso para registrar una serie dentro de una sesión.
 * @property eliminarRutinaUseCase Caso de uso para eliminar una rutina.
 * @property obtenerEstadisticasPorEjercicioUseCase Caso de uso para obtener estadísticas de series por ejercicio.
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

    /** Lista reactiva de rutinas obtenidas para el usuario. */
    private val _rutinas = MutableStateFlow<List<RutinaEntity>>(emptyList())
    val rutinas: StateFlow<List<RutinaEntity>> get() = _rutinas

    /** Lista reactiva de ejercicios dentro de una rutina. */
    private val _ejerciciosEnRutina = MutableStateFlow<List<EjercicioEnRutinaEntity>>(emptyList())
    val ejerciciosEnRutina: StateFlow<List<EjercicioEnRutinaEntity>> get() = _ejerciciosEnRutina

    /** Lista reactiva de ejercicios predefinidos disponibles para agregar. */
    private val _ejerciciosPredefinidos = MutableStateFlow<List<EjercicioPredefinidoEntity>>(emptyList())
    val ejerciciosPredefinidos: StateFlow<List<EjercicioPredefinidoEntity>> get() = _ejerciciosPredefinidos

    /** Estado para mensajes de error o información que la UI debe mostrar. */
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> get() = _mensaje

    /** Lista reactiva con registros de series realizadas para un ejercicio específico. */
    private val _seriesPorEjercicio = MutableStateFlow<List<RegistroSerieSesionEntity>>(emptyList())
    val seriesPorEjercicio: StateFlow<List<RegistroSerieSesionEntity>> get() = _seriesPorEjercicio

    /**
     * Carga las rutinas del usuario identificado por [userId].
     */
    fun cargarRutinas(userId: Int) {
        viewModelScope.launch {
            try {
                _rutinas.value = obtenerRutinasUseCase(userId)
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar rutinas: ${e.message}"
            }
        }
    }

    /**
     * Crea una nueva rutina con [nombre] para el usuario [userId].
     * Luego recarga la lista de rutinas.
     */
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

    /**
     * Carga los ejercicios asociados a la rutina [rutinaId].
     */
    fun cargarEjerciciosEnRutina(rutinaId: Int) {
        viewModelScope.launch {
            try {
                _ejerciciosEnRutina.value = obtenerRutinasUseCase.obtenerEjerciciosDeRutina(rutinaId)
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar ejercicios: ${e.message}"
            }
        }
    }

    /**
     * Carga la lista de ejercicios predefinidos disponibles para agregar a rutinas.
     */
    fun cargarEjerciciosPredefinidos() {
        viewModelScope.launch {
            try {
                _ejerciciosPredefinidos.value = obtenerRutinasUseCase.obtenerEjerciciosPredefinidos()
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar ejercicios predefinidos: ${e.message}"
            }
        }
    }

    /**
     * Agrega un ejercicio predefinido [ejercicioPredefinidoId] a la rutina [rutinaId].
     * Luego recarga los ejercicios de la rutina.
     */
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

    /**
     * Inicia una sesión para la rutina [rutinaId].
     *
     * @param onSesionCreada Callback que recibe el ID de la sesión creada.
     */
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

    /**
     * Registra una serie en la sesión de rutina con los datos proporcionados.
     */
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

    /**
     * Elimina la rutina indicada y recarga la lista de rutinas.
     * Ejecuta [onDone] al completar la operación.
     */
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

    /**
     * Carga las series registradas para un ejercicio específico.
     */
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

    /**
     * Limpia el mensaje mostrado en la UI.
     */
    fun limpiarMensaje() {
        _mensaje.value = null
    }
}