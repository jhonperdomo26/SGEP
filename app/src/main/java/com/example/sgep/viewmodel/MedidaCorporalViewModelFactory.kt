package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgep.domain.usecase.MedidaCorporalUseCase

/**
 * Factory para crear instancias de MedidaCorporalViewModel con las dependencias necesarias.
 * Permite la inyección de dependencias en el ViewModel.
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