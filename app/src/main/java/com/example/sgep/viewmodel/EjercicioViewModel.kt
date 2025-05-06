package com.example.sgep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.dao.EjercicioDao
import com.example.sgep.data.entity.EjercicioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EjercicioViewModel(private val ejercicioDao: EjercicioDao) : ViewModel() {

    private val _ejercicios = MutableStateFlow<List<EjercicioEntity>>(emptyList())
    val ejercicios: StateFlow<List<EjercicioEntity>> get() = _ejercicios

    fun cargarEjercicios() {
        viewModelScope.launch {
            _ejercicios.value = ejercicioDao.obtenerTodosLosEjercicios()
        }
    }

    fun agregarEjercicio(ejercicio: EjercicioEntity) {
        viewModelScope.launch {
            ejercicioDao.insertarEjercicio(ejercicio)
            cargarEjercicios() // Actualiza la lista después de insertar
        }
    }
}