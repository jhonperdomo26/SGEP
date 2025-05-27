package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.EjercicioPredefinidoEntity

/**
 * DAO para acceder a los ejercicios predefinidos disponibles en el sistema.
 * Estos ejercicios son cargados por defecto y normalmente no se modifican durante el uso.
 */
@Dao
interface EjercicioPredefinidoDao {

    /**
     * Inserta una lista de ejercicios predefinidos en la base de datos.
     * Si ya existen, se reemplazan según la estrategia [OnConflictStrategy.REPLACE].
     * Útil para poblar la base de datos al inicio.
     *
     * @param ejercicios Lista de objetos [EjercicioPredefinidoEntity] a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ejercicios: List<EjercicioPredefinidoEntity>)

    /**
     * Recupera todos los ejercicios predefinidos almacenados en la base de datos.
     *
     * @return Lista de [EjercicioPredefinidoEntity] disponibles.
     */
    @Query("SELECT * FROM ejercicio_predefinido")
    suspend fun getAll(): List<EjercicioPredefinidoEntity>
}