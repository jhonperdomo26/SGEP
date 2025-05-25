package com.example.sgep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sgep.ui.navigation.Navigation
import com.example.sgep.ui.theme.AppTheme
import com.example.sgep.viewmodel.LoginViewModel
import com.example.sgep.viewmodel.LoginViewModelFactory
import com.example.sgep.viewmodel.RegisterViewModel
import com.example.sgep.viewmodel.RegisterViewModelFactory
import com.example.sgep.viewmodel.RutinaViewModel
import com.example.sgep.viewmodel.RutinaViewModelFactory
import com.example.sgep.data.database.AppDatabase
import com.example.sgep.data.entity.EjercicioPredefinidoEntity
import com.example.sgep.data.repository.RutinaRepository
import com.example.sgep.data.repository.SesionRutinaRepository
import com.example.sgep.domain.usecase.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// --- FUNCIÓN PARA POBLAR EJERCICIOS PREDEFINIDOS ---
fun poblarEjerciciosPredefinidos(db: AppDatabase) {
    GlobalScope.launch {
        val ejerciciosExistentes = db.ejercicioPredefinidoDao().getAll()
        if (ejerciciosExistentes.isEmpty()) {
            val defaultEjercicios = listOf(
                EjercicioPredefinidoEntity(
                    nombre = "Sentadilla",
                    grupoMuscular = "Piernas",
                    descripcion = "De pie, pies a la anchura de los hombros. Baja flexionando rodillas y caderas, espalda recta."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Press de banca",
                    grupoMuscular = "Pecho",
                    descripcion = "Tumbado en banco, barra a la altura del pecho, baja y sube controladamente."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Peso muerto",
                    grupoMuscular = "Espalda baja y piernas",
                    descripcion = "Barra en el suelo, espalda recta, sube usando caderas y piernas."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Press militar",
                    grupoMuscular = "Hombros",
                    descripcion = "De pie o sentado, empuja la barra por encima de la cabeza desde las clavículas."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Remo con barra",
                    grupoMuscular = "Espalda",
                    descripcion = "Inclina el torso, agarra la barra y lleva al abdomen."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Curl de bíceps",
                    grupoMuscular = "Bíceps",
                    descripcion = "De pie, barra o mancuernas, sube solo flexionando codo."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Dominadas",
                    grupoMuscular = "Espalda",
                    descripcion = "Agarra barra con pronación, sube el mentón por encima de la barra."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Crunch abdominal",
                    grupoMuscular = "Abdomen",
                    descripcion = "Tumbado boca arriba, flexiona el tronco para acercar pecho a rodillas."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Fondos",
                    grupoMuscular = "Tríceps y pecho",
                    descripcion = "Cuerpo suspendido, baja y sube flexionando codos."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Extensión de tríceps",
                    grupoMuscular = "Tríceps",
                    descripcion = "De pie o sentado, extiende los codos con mancuerna o barra."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Elevaciones laterales",
                    grupoMuscular = "Hombros",
                    descripcion = "De pie, eleva los brazos lateralmente con mancuernas."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Zancadas",
                    grupoMuscular = "Piernas",
                    descripcion = "Da un paso hacia adelante y baja la rodilla trasera hacia el suelo."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Gemelos",
                    grupoMuscular = "Pantorrillas",
                    descripcion = "De pie, eleva los talones manteniendo las puntas de los pies en el suelo."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Facepull",
                    grupoMuscular = "Hombros y espalda alta",
                    descripcion = "Jala una cuerda hacia la cara manteniendo codos altos."
                ),
            )
            db.ejercicioPredefinidoDao().insertAll(defaultEjercicios)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(applicationContext)
        poblarEjerciciosPredefinidos(db) // Poblar ejercicios al iniciar

        setContent {
            AppTheme {
                // LoginViewModel instanciado
                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(application)
                )
                //RegisterViewModel instanciado
                val registerViewModel: RegisterViewModel = viewModel(
                    factory = RegisterViewModelFactory(application)
                )
                // Instancia la base de datos y repositorios
                val rutinaRepository = RutinaRepository(
                    rutinaDao = db.rutinaDao(),
                    ejercicioEnRutinaDao = db.ejercicioEnRutinaDao(),
                    serieEjercicioDao = db.serieEjercicioDao(),
                    ejercicioPredefinidoDao = db.ejercicioPredefinidoDao()
                )
                val sesionRutinaRepository = SesionRutinaRepository(
                    sesionRutinaDao = db.sesionRutinaDao(),
                    registroSerieSesionDao = db.registroSerieSesionDao()
                )

                // Casos de uso
                val crearRutinaUseCase = CrearRutinaUseCase(rutinaRepository)
                val obtenerRutinasUseCase = ObtenerRutinasUseCase(rutinaRepository)
                val agregarEjercicioARutinaUseCase = AgregarEjercicioARutinaUseCase(rutinaRepository)
                val iniciarSesionRutinaUseCase = IniciarSesionRutinaUseCase(sesionRutinaRepository)
                val registrarSerieSesionUseCase = RegistrarSerieSesionUseCase(sesionRutinaRepository)
                val eliminarRutinaUseCase = EliminarRutinaUseCase(rutinaRepository)
                val obtenerEstadisticasPorEjercicioUseCase = ObtenerEstadisticasPorEjercicioUseCase(sesionRutinaRepository)

                // RutinaViewModel con todos los casos de uso, incluyendo el de eliminar
                val rutinaViewModel: RutinaViewModel = viewModel(
                    factory = RutinaViewModelFactory(
                        crearRutinaUseCase,
                        obtenerRutinasUseCase,
                        agregarEjercicioARutinaUseCase,
                        iniciarSesionRutinaUseCase,
                        registrarSerieSesionUseCase,
                        eliminarRutinaUseCase,
                        obtenerEstadisticasPorEjercicioUseCase
                    )
                )

                // Pasa ambos ViewModels a Navigation
                Navigation(
                    loginViewModel = loginViewModel,
                    registerViewModel = registerViewModel,
                    rutinaViewModel = rutinaViewModel
                )
            }
        }
    }
}
