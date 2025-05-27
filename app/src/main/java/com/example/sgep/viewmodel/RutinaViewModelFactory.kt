package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgep.domain.usecase.*

/**
 * RutinaViewModelFactory es una fábrica personalizada para crear instancias de [RutinaViewModel].
 *
 * Permite inyectar los casos de uso necesarios para el ViewModel,
 * facilitando la separación de responsabilidades y el testing.
 *
 * @property crearRutinaUseCase Caso de uso para crear una rutina.
 * @property obtenerRutinasUseCase Caso de uso para obtener las rutinas del usuario.
 * @property agregarEjercicioARutinaUseCase Caso de uso para agregar un ejercicio predefinido a una rutina.
 * @property iniciarSesionRutinaUseCase Caso de uso para iniciar una sesión de rutina.
 * @property registrarSerieSesionUseCase Caso de uso para registrar una serie dentro de una sesión.
 * @property eliminarRutinaUseCase Caso de uso para eliminar una rutina.
 * @property obtenerEstadisticasPorEjercicioUseCase Caso de uso para obtener estadísticas de ejercicios.
 *
 * @throws IllegalArgumentException Si se intenta crear un ViewModel de una clase desconocida.
 */
class RutinaViewModelFactory(
    private val crearRutinaUseCase: CrearRutinaUseCase,
    private val obtenerRutinasUseCase: ObtenerRutinasUseCase,
    private val agregarEjercicioARutinaUseCase: AgregarEjercicioARutinaUseCase,
    private val iniciarSesionRutinaUseCase: IniciarSesionRutinaUseCase,
    private val registrarSerieSesionUseCase: RegistrarSerieSesionUseCase,
    private val eliminarRutinaUseCase: EliminarRutinaUseCase,
    private val obtenerEstadisticasPorEjercicioUseCase: ObtenerEstadisticasPorEjercicioUseCase

) : ViewModelProvider.Factory {

    /**
     * Crea una instancia de [RutinaViewModel] con los casos de uso inyectados.
     *
     * @param modelClass Clase del ViewModel a crear.
     * @return Instancia del ViewModel solicitado.
     * @throws IllegalArgumentException Si el ViewModel solicitado no es [RutinaViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RutinaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RutinaViewModel(
                crearRutinaUseCase,
                obtenerRutinasUseCase,
                agregarEjercicioARutinaUseCase,
                iniciarSesionRutinaUseCase,
                registrarSerieSesionUseCase,
                eliminarRutinaUseCase,
                obtenerEstadisticasPorEjercicioUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}