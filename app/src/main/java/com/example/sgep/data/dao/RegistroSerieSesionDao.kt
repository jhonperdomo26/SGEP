package com.example.sgep.data.dao

import androidx.room.*
import com.example.sgep.data.entity.RegistroSerieSesionEntity

/**
 * Aquí almacenamos lo que realmente hizo el usuario en cada serie durante una sesión.
 * Así puedes analizar el desempeño real.
 */
@Dao
interface RegistroSerieSesionDao {
    @Insert
    suspend fun insert(registro: RegistroSerieSesionEntity): Long

    @Query("SELECT * FROM registro_serie_sesion WHERE sesionRutinaId = :sesionRutinaId AND ejercicioEnRutinaId = :ejercicioEnRutinaId")
    suspend fun getBySesionAndEjercicio(sesionRutinaId: Int, ejercicioEnRutinaId: Int): List<RegistroSerieSesionEntity>

    @Query("SELECT * FROM registro_serie_sesion WHERE sesionRutinaId = :sesionRutinaId")
    suspend fun getBySesion(sesionRutinaId: Int): List<RegistroSerieSesionEntity>

    @Query("SELECT * FROM registro_serie_sesion WHERE ejercicioEnRutinaId = :ejercicioEnRutinaId")
    suspend fun getByEjercicioEnRutinaId(ejercicioEnRutinaId: Int): List<RegistroSerieSesionEntity>
}
