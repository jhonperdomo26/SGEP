package com.example.sgep.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sgep.data.converters.EjercicioListConverter
import com.example.sgep.data.dao.EjercicioDao
import com.example.sgep.data.dao.RutinaDao
import com.example.sgep.data.dao.UserDao
import com.example.sgep.data.entity.EjercicioEntity
import com.example.sgep.data.entity.RutinaEntity
import com.example.sgep.data.entity.UserEntity

<<<<<<< Updated upstream
@Database(entities = [UserEntity::class], version = 2, exportSchema = false)
=======
// Incrementar la versión de la base de datos y añadir las nuevas entidades.
@Database(
    entities = [
        UserEntity::class,
        // Si tienes EjercicioEntity y EjercicioDao, añádelos aquí también:
        // EjercicioEntity::class,
        RutinaEntity::class // *** Añadido RutinaEntity ***
    ],
    version = 2, // *** IMPORTANTE: Incrementa la versión (de 1 a 2) ***
    exportSchema = false
)
// Si manejas listas de ejercicios en RutinaEntity, necesitas un TypeConverter:
@TypeConverters(EjercicioListConverter::class) // *** Asegúrate de tener esta anotación si usas ExerciseListConverter ***
>>>>>>> Stashed changes
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    // Si tienes EjercicioDao, descomenta o asegúrate de tener este método:
    // abstract fun ejercicioDao(): EjercicioDao
    abstract fun rutinaDao(): RutinaDao // *** Añadido método para RutinaDao ***

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sgep_database"
                )
                    // Como incrementamos la versión, necesitamos manejar la migración.
                    // .fallbackToDestructiveMigration() es una opción simple si no te importa perder datos
                    // en instalaciones existentes al actualizar la base de datos.
                    // Si te importan los datos, deberías implementar una migración adecuada en lugar de fallbackToDestructiveMigration().
                    .fallbackToDestructiveMigration() // Mantenemos esto por ahora, pero sé consciente de sus implicaciones.
                    .build()
                INSTANCE = instance
                instance
            }
        }
        // Si decides implementar migraciones manuales en el futuro, las definirías aquí.
    }
}
