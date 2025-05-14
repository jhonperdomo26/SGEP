package com.example.sgep.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.sgep.data.dao.RutinaDao
import com.example.sgep.data.entity.EjercicioEntity
import com.example.sgep.data.entity.RutinaEntity

class EjercicioViewModel(
    private val rutinaDao: RutinaDao
) : ViewModel() {

    // Lista de ejercicios predeterminados (en memoria)
    private val _listaEjerciciosPredeterminados = listOf(
        EjercicioEntity(nombre = "Press de Banca", grupoMuscular = "Pecho", tipo = "Pesas Libres", posicion = "Tumbado"),
        EjercicioEntity(nombre = "Sentadilla", grupoMuscular = "Pierna", tipo = "Pesas Libres", posicion = "De Pie"),
        EjercicioEntity(nombre = "Peso Muerto", grupoMuscular = "Espalda", tipo = "Pesas Libres", posicion = "De Pie"),
        EjercicioEntity(nombre = "Dominadas", grupoMuscular = "Espalda", tipo = "Peso Corporal", posicion = "Colgado"),
        EjercicioEntity(nombre = "Fondos", grupoMuscular = "Pecho/Tríceps", tipo = "Peso Corporal", posicion = "Barras Paralelas"),
        EjercicioEntity(nombre = "Remo con Barra", grupoMuscular = "Espalda", tipo = "Pesas Libres", posicion = "De Pie/Sentado"),
        EjercicioEntity(nombre = "Press Militar", grupoMuscular = "Hombro", tipo = "Pesas Libres", posicion = "De Pie/Sentado"),
        EjercicioEntity(nombre = "Curl de Bíceps", grupoMuscular = "Bíceps", tipo = "Pesas Libres", posicion = "De Pie/Sentado"),
        EjercicioEntity(nombre = "Extensión de Tríceps", grupoMuscular = "Tríceps", tipo = "Pesas Libres", posicion = "De Pie/Tumbado"),
        EjercicioEntity(nombre = "Zancadas", grupoMuscular = "Pierna", tipo = "Pesas Libres", posicion = "De Pie")
    )

    val listaEjerciciosPredeterminados: List<EjercicioEntity> get() = _listaEjerciciosPredeterminados

    /**
     * Guarda una rutina en la base de datos.
     * @param rutina La rutina a guardar.
     */
    fun guardarRutina(rutina: RutinaEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            rutinaDao.insert(rutina)
        }
    }

    // *** Nueva propiedad para exponer todas las rutinas ***
    // Observa el Flow del DAO y lo convierte a LiveData
    val todasLasRutinas: LiveData<List<RutinaEntity>> = rutinaDao.getAllRutinas().asLiveData()

    // Puedes añadir aquí otras funciones relacionadas con ejercicios o rutinas si las tienes.
}
