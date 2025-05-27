package com.example.sgep.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sgep.data.dao.*
import com.example.sgep.data.entity.*

/**
 * Clase abstracta que representa la base de datos Room de la aplicación SGEP.
 *
 * Contiene todas las entidades de la base de datos y proporciona acceso a los DAOs correspondientes.
 *
 * Implementa el patrón singleton para garantizar una única instancia durante la vida de la aplicación.
 */
@Database(
    entities = [
        UserEntity::class,
        RutinaEntity::class,
        EjercicioPredefinidoEntity::class,
        EjercicioEnRutinaEntity::class,
        SerieEjercicioEntity::class,
        SesionRutinaEntity::class,
        RegistroSerieSesionEntity::class,
        MedidaCorporalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /** DAO para la entidad Usuario */
    abstract fun userDao(): UserDao

    /** DAO para la entidad Rutina */
    abstract fun rutinaDao(): RutinaDao

    /** DAO para la entidad Ejercicio Predefinido */
    abstract fun ejercicioPredefinidoDao(): EjercicioPredefinidoDao

    /** DAO para la entidad Ejercicio en Rutina */
    abstract fun ejercicioEnRutinaDao(): EjercicioEnRutinaDao

    /** DAO para la entidad Serie de Ejercicio */
    abstract fun serieEjercicioDao(): SerieEjercicioDao

    /** DAO para la entidad Sesión de Rutina */
    abstract fun sesionRutinaDao(): SesionRutinaDao

    /** DAO para la entidad Registro de Serie de Sesión */
    abstract fun registroSerieSesionDao(): RegistroSerieSesionDao

    /** DAO para la entidad Medida Corporal */
    abstract fun medidaCorporalDao(): MedidaCorporalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia singleton de la base de datos.
         *
         * @param context Contexto de la aplicación para construir la base de datos.
         * @return Instancia única de AppDatabase.
         */
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
                            // Habilita la integridad referencial en SQLite (claves foráneas)
                            db.execSQL("PRAGMA foreign_keys=ON;")
                        }
                    })
                    // En desarrollo, permite destruir y reconstruir la base para evitar conflictos de migración
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}