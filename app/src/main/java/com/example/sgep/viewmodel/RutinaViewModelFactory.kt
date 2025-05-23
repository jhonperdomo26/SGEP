package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgep.domain.usecase.*

/**
 * RutinaViewModelFactory permite crear el ViewModel pasándole los casos de uso necesarios.
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
