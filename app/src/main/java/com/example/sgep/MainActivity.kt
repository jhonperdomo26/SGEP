package com.example.sgep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sgep.ui.navigation.Navigation
import com.example.sgep.ui.theme.AppTheme
import com.example.sgep.viewmodel.LoginViewModel
import com.example.sgep.viewmodel.LoginViewModelFactory
import com.example.sgep.viewmodel.RutinaViewModel
import com.example.sgep.viewmodel.RutinaViewModelFactory
import com.example.sgep.data.database.AppDatabase
import com.example.sgep.data.entity.EjercicioPredefinidoEntity
import com.example.sgep.data.repository.MedidaCorporalRepository
import com.example.sgep.data.repository.RutinaRepository
import com.example.sgep.data.repository.SesionRutinaRepository
import com.example.sgep.domain.usecase.*
import com.example.sgep.viewmodel.MedidaCorporalViewModel
import com.example.sgep.viewmodel.MedidaCorporalViewModelFactory
import com.example.sgep.viewmodel.RegisterViewModel
import com.example.sgep.viewmodel.RegisterViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Función para poblar la base de datos con ejercicios predefinidos.
 * Si la tabla de ejercicios está vacía, inserta una lista por defecto de ejercicios con su
 * nombre, grupo muscular y descripción básica.
 *
 * @param db instancia de la base de datos.
 */
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
                    nombre = "Press de banca plano",
                    grupoMuscular = "Pecho",
                    descripcion = "Tumbado en banco, barra a la altura del pecho, baja y sube controladamente."
                ),
                EjercicioPredefinidoEntity(
                    nombre = "Press de banca inclinado",
                    grupoMuscular = "Pecho",
                    descripcion = "Tumbado en banco inclinado, barra a la altura del pecho, baja y sube controladamente."
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

/**
 * MainActivity es el punto de entrada principal de la aplicación.
 * - Inicializa la base de datos.
 * - Pobla la base de datos con ejercicios predefinidos si es necesario.
 * - Configura los ViewModels para las funcionalidades principales (login, registro, rutinas, medidas corporales).
 * - Ejecuta la composición de UI con Jetpack Compose, aplicando el tema y la navegación principal.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener instancia de la base de datos
        val db = AppDatabase.getDatabase(applicationContext)

        // Poblar ejercicios predefinidos si la base de datos está vacía
        poblarEjerciciosPredefinidos(db)

        setContent {
            AppTheme {

                // Instancia ViewModels usando las factories correspondientes
                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(application)
                )
                val registerViewModel: RegisterViewModel = viewModel(
                    factory = RegisterViewModelFactory(application)
                )

                // Observa el usuario actual logueado desde LoginViewModel
                val currentUser by loginViewModel.currentUser.collectAsState()

                // Crear repositorios necesarios con DAOs de la base de datos
                val medidaCorporalRepository = MedidaCorporalRepository(db.medidaCorporalDao())
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

                // Crear casos de uso que operan con los repositorios
                val medidaCorporalUseCase = MedidaCorporalUseCase(medidaCorporalRepository)
                val crearRutinaUseCase = CrearRutinaUseCase(rutinaRepository)
                val obtenerRutinasUseCase = ObtenerRutinasUseCase(rutinaRepository)
                val agregarEjercicioARutinaUseCase = AgregarEjercicioARutinaUseCase(rutinaRepository)
                val iniciarSesionRutinaUseCase = IniciarSesionRutinaUseCase(sesionRutinaRepository)
                val registrarSerieSesionUseCase = RegistrarSerieSesionUseCase(sesionRutinaRepository)
                val eliminarRutinaUseCase = EliminarRutinaUseCase(rutinaRepository)
                val obtenerEstadisticasPorEjercicioUseCase = ObtenerEstadisticasPorEjercicioUseCase(sesionRutinaRepository)

                // Configurar ViewModel para medidas corporales con su caso de uso
                val medidaCorporalViewModel: MedidaCorporalViewModel = viewModel(
                    factory = MedidaCorporalViewModelFactory(medidaCorporalUseCase)
                )

                // Actualizar medidas cuando cambie el usuario
                LaunchedEffect(currentUser) {
                    currentUser?.let { user ->
                        medidaCorporalViewModel.cargarMedidas(user.id)
                    }
                }

                // Configurar ViewModel para rutinas con todos los casos de uso relacionados
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

                // Configurar la navegación principal pasando los ViewModels necesarios
                Navigation(
                    loginViewModel = loginViewModel,
                    registerViewModel = registerViewModel,
                    rutinaViewModel = rutinaViewModel,
                    medidaCorporalViewModel = medidaCorporalViewModel
                )
            }
        }
    }
}