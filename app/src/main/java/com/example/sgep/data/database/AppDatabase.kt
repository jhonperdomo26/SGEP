package com.example.sgep.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sgep.data.dao.*
import com.example.sgep.data.entity.*

@Database(
    entities = [
        UserEntity::class,
        RutinaEntity::class,
        EjercicioPredefinidoEntity::class,
        EjercicioEnRutinaEntity::class,
        SerieEjercicioEntity::class,
        SesionRutinaEntity::class,
        RegistroSerieSesionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun userDao(): UserDao
    abstract fun rutinaDao(): RutinaDao
    abstract fun ejercicioPredefinidoDao(): EjercicioPredefinidoDao
    abstract fun ejercicioEnRutinaDao(): EjercicioEnRutinaDao
    abstract fun serieEjercicioDao(): SerieEjercicioDao
    abstract fun sesionRutinaDao(): SesionRutinaDao
    abstract fun registroSerieSesionDao(): RegistroSerieSesionDao

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
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            // Activar claves foráneas en SQLite
                            db.execSQL("PRAGMA foreign_keys=ON;")
                        }
                    })
                    .fallbackToDestructiveMigration() // útil para desarrollo
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
