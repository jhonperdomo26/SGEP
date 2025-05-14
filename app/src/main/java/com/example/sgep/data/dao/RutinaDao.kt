package com.example.sgep.data.dao // Asegúrate de que este paquete sea correcto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sgep.data.entity.RutinaEntity
import kotlinx.coroutines.flow.Flow // Importa Flow

@Dao
interface RutinaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(rutina: RutinaEntity): Long

    @Query("SELECT * FROM routines WHERE id = :id LIMIT 1")
    suspend fun getRutinaById(id: Int): RutinaEntity?

    // *** Nuevo método para obtener todas las rutinas como Flow ***
    @Query("SELECT * FROM routines ORDER BY fechaCreacion DESC") // Puedes ordenar como prefieras
    fun getAllRutinas(): Flow<List<RutinaEntity>>
}
