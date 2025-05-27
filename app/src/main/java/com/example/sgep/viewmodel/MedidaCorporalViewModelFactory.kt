package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgep.domain.usecase.MedidaCorporalUseCase

/**
 * Factory para crear instancias de [MedidaCorporalViewModel] con las dependencias necesarias.
 *
 * Este factory permite inyectar el caso de uso [MedidaCorporalUseCase] en el ViewModel.
 *
 * @property useCase Caso de uso para manejar la lógica de medidas corporales.
 */
class MedidaCorporalViewModelFactory(
    private val useCase: MedidaCorporalUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedidaCorporalViewModel::class.java)) {
            return MedidaCorporalViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}