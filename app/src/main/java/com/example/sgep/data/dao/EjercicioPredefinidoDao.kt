package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.EjercicioPredefinidoEntity

/**
 * Dao para acceder a la tabla de ejercicios predefinidos.
 * Normalmente solo la usamos para leer, porque estos ejercicios los cargamos nosotros.
 */
@Dao
interface EjercicioPredefinidoDao {
    // Insertar varios ejercicios de golpe (puedes usar esto para poblar la base de datos)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ejercicios: List<EjercicioPredefinidoEntity>)

    // Obtener todos los ejercicios predefinidos
    @Query("SELECT * FROM ejercicio_predefinido")
    suspend fun getAll(): List<EjercicioPredefinidoEntity>

    // Buscar por nombre (útil para buscar desde la UI)
    @Query("SELECT * FROM ejercicio_predefinido WHERE nombre LIKE '%' || :nombre || '%'")
    suspend fun searchByName(nombre: String): List<EjercicioPredefinidoEntity>
}
