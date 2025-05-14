package com.example.sgep.viewmodel // Asegúrate de que este paquete sea correcto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgep.data.dao.RutinaDao // Importa RutinaDao


class EjercicioViewModelFactory(
    private val rutinaDao: RutinaDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EjercicioViewModel::class.java)) {
            return EjercicioViewModel(rutinaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
