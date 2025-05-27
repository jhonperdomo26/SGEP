package com.example.sgep.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory para crear instancias de [RegisterViewModel] con el parámetro [Application].
 *
 * Esta clase implementa [ViewModelProvider.Factory] para permitir la
 * creación de un [RegisterViewModel] que requiere un [Application] en su constructor.
 *
 * @property application La instancia de [Application] que será pasada al ViewModel.
 */
class RegisterViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    /**
     * Crea una instancia del ViewModel especificado.
     *
     * @param T El tipo de ViewModel.
     * @param modelClass La clase del ViewModel que se desea crear.
     * @return Una instancia del ViewModel solicitada.
     * @throws IllegalArgumentException Si la clase ViewModel no es soportada por este Factory.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(application) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
    }
}