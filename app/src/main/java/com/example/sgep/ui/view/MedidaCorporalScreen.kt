package com.example.sgep.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.sgep.data.dao.errors.MedidasError
import com.example.sgep.data.entity.MedidaCorporalEntity
import com.example.sgep.viewmodel.MedidaCorporalViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla de registro que permite a un usuario crear una cuenta.
 *
 * @param medidaCorporalViewModel ViewModel para manejar la lógica de las medidas.
 * @param onBack Lambda que se ejecuta para regresar a la pantalla anterior.
 * @param userId Identificador del usuario actual, para cargar sus medidas.
 * @param modifier Modifier opcional para personalizar el layout externo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedidaCorporalScreen(
    viewModel: MedidaCorporalViewModel,
    userId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showNewMeasureDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val error by viewModel.error.collectAsState()
    val medidas by viewModel.medidas.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val diferencias by viewModel.diferencias.collectAsState()

    // Cargar medidas al entrar
    LaunchedEffect(userId) {
        viewModel.cargarMedidas(userId)
    }

    // Mostrar errores
    error?.let { e ->
        LaunchedEffect(e) {
            SnackbarHostState().showSnackbar(e.message ?: "Ocurrió un error")
            viewModel.limpiarError()
        }
    }

    if (mensaje != null) {
        LaunchedEffect(mensaje) {
            snackbarHostState.showSnackbar(mensaje!!)
            viewModel.limpiarMensaje()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.background
                    ),
                    startY = 0f,
                    endY = 1000f
                )
            )
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Medidas Corporales") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showNewMeasureDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva medida")
                }
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                diferencias?.let { difs ->
                    ComparisonCard(difs) {
                        viewModel.limpiarDiferencias()
                    }
                }

                MeasuresList(
                    medidas = medidas,
                    onMeasureClick = { medida -> viewModel.seleccionarMedida(medida.id) },
                    onCompareClick = { medida ->
                        medidas.getOrNull(medidas.indexOf(medida) + 1)?.let { anterior ->
                            viewModel.calcularDiferencias(medida, anterior)
                        }
                    }
                )
            }
        }

        if (showNewMeasureDialog) {
            NewMeasureDialog(
                userId = userId,
                viewModel = viewModel,
                onDismiss = { showNewMeasureDialog = false },
                onSave = { nuevaMedida ->
                    viewModel.registrarMedida(userId, nuevaMedida)
                }
            )
        }
    }
}

@Composable
fun ComparisonCard(
    differences: Map<String, Float>,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Comparación con medida anterior",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar comparación",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Medidas comparadas
            differences.forEach { (field, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = field.split(" ").joinToString(" ") {
                            it.replaceFirstChar(Char::uppercase)
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )

                    val unidad = if (field == "peso") "kg" else "cm"
                    val textoValor = if (value >= 0) "+${"%.1f".format(value)} $unidad" else "${"%.1f".format(value)} $unidad"

                    Text(
                        text = textoValor,
                        color = when {
                            value > 0 -> MaterialTheme.colorScheme.secondary
                            value < 0 -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                }
            }
        }
    }
}

@Composable
private fun MeasuresList(
    medidas: List<MedidaCorporalEntity>,
    onMeasureClick: (MedidaCorporalEntity) -> Unit,
    onCompareClick: (MedidaCorporalEntity) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(medidas) { medida ->
            MeasureItem(
                medida = medida,
                dateFormat = dateFormat,
                onClick = { onMeasureClick(medida) },
                onCompare = { onCompareClick(medida) }
            )
        }
    }
}

@Composable
private fun MeasureItem(
    medida: MedidaCorporalEntity,
    dateFormat: SimpleDateFormat,
    onClick: () -> Unit,
    onCompare: () -> Unit
) {
    val fechaFormateada = dateFormat.format(Date(medida.fecha))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Fecha y peso en fila superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fechaFormateada,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${"%.1f".format(medida.peso)} kg",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scroll horizontal para las medidas corporales (para que no quede apretado)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Función auxiliar para mostrar cada medida con etiqueta y valor
                @Composable
                fun MedidaText(label: String, value: Double) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "${"%.1f".format(value)} cm",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                MedidaText("Cuello", medida.cuello.toDouble())
                MedidaText("Hombros", medida.hombros.toDouble())
                MedidaText("Pecho", medida.pecho.toDouble())
                MedidaText("Cintura", medida.cintura.toDouble())
                MedidaText("Cadera", medida.cadera.toDouble())
                MedidaText("Glúteos", medida.gluteos.toDouble())
                MedidaText("Muslo Izq", medida.musloIzq.toDouble())
                MedidaText("Muslo Der", medida.musloDer.toDouble())
                MedidaText("Gemelo Izq", medida.gemeloIzq.toDouble())
                MedidaText("Gemelo Der", medida.gemeloDer.toDouble())
                MedidaText("Bíceps Izq", medida.bicepsIzq.toDouble())
                MedidaText("Bíceps Der", medida.bicepsDer.toDouble())
                MedidaText("Antebrazo Izq", medida.antebrazoIzq.toDouble())
                MedidaText("Antebrazo Der", medida.antebrazoDer.toDouble())
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón comparar alineado a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onCompare,
                    modifier = Modifier.shadow(4.dp, shape = MaterialTheme.shapes.small),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("Comparar")
                }
            }
        }
    }
}

@Composable
private fun NewMeasureDialog(
    userId: Int,
    viewModel: MedidaCorporalViewModel,
    onDismiss: () -> Unit,
    onSave: (MedidaCorporalEntity) -> Unit
) {
    // Estados para cada campo de medida
    var peso by remember { mutableStateOf("") }
    var cuello by remember { mutableStateOf("") }
    var hombros by remember { mutableStateOf("") }
    var pecho by remember { mutableStateOf("") }
    var cintura by remember { mutableStateOf("") }
    var cadera by remember { mutableStateOf("") }
    var gluteos by remember { mutableStateOf("") }
    var musloIzq by remember { mutableStateOf("") }
    var musloDer by remember { mutableStateOf("") }
    var gemeloIzq by remember { mutableStateOf("") }
    var gemeloDer by remember { mutableStateOf("") }
    var bicepsIzq by remember { mutableStateOf("") }
    var bicepsDer by remember { mutableStateOf("") }
    var antebrazoIzq by remember { mutableStateOf("") }
    var antebrazoDer by remember { mutableStateOf("") }

    // Validación básica de campos requeridos
    val camposRequeridos = listOf(peso, cuello, hombros, pecho,
        cintura, cadera, gluteos, musloIzq, musloDer, gemeloIzq,
        gemeloDer, bicepsIzq, bicepsDer, antebrazoIzq, antebrazoDer)
    val camposValidos = camposRequeridos.all { it.isNotBlank() && it.toFloatOrNull() != null }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Medida Corporal") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sección: Medidas principales
                Text("Medidas Principales", style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = peso,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            peso = it
                        }
                    },
                    label = { Text("Peso (kg)*") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Sección: Tronco
                Text("Tronco", style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = cuello,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            cuello = it
                        }
                    },
                    label = { Text("Cuello (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = hombros,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            hombros = it
                        }
                    },
                    label = { Text("Hombros (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = pecho,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            pecho = it
                        }
                    },
                    label = { Text("Pecho (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cintura,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            cintura = it
                        }
                    },
                    label = { Text("Cintura (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cadera,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            cadera = it
                        }
                    },
                    label = { Text("Cadera (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = gluteos,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            gluteos = it
                        }
                    },
                    label = { Text("Glúteos (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Sección: Extremidades
                Text("Extremidades", style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = musloIzq,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            musloIzq = it
                        }
                    },
                    label = { Text("Muslo Izquierdo (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = musloDer,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            musloDer = it
                        }
                    },
                    label = { Text("Muslo Derecho (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = gemeloIzq,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            gemeloIzq = it
                        }
                    },
                    label = { Text("Gemelo Izquierdo (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = gemeloDer,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            gemeloDer = it
                        }
                    },
                    label = { Text("Gemelo Derecho (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bicepsIzq,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            bicepsIzq = it
                        }
                    },
                    label = { Text("Bíceps Izquierdo (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bicepsDer,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            bicepsDer = it
                        }
                    },
                    label = { Text("Bíceps Derecho (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = antebrazoIzq,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            antebrazoIzq = it
                        }
                    },
                    label = { Text("Antebrazo Izquierdo (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = antebrazoDer,
                    onValueChange = {
                        if (it.isEmpty() || it.toFloatOrNull()?.let { v -> v >= 0 } == true) {
                            antebrazoDer = it
                        }
                    },
                    label = { Text("Antebrazo Derecho (cm)*") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    try {
                        val medida = MedidaCorporalEntity(
                            userId = userId,
                            fecha = System.currentTimeMillis(),
                            peso = peso.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Peso"),
                            cintura = cintura.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Cintura"),
                            cuello = cuello.toFloatOrNull()?.takeIf { it > 0 } ?:
                            throw MedidasError.ValorNegativo("Cuello"),
                            hombros = hombros.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Hombros"),
                            pecho = pecho.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Pecho"),
                            cadera = cadera.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Cadera"),
                            gluteos = gluteos.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Gluteos"),
                            musloIzq = musloIzq.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Muslo izquierda"),
                            musloDer = musloDer.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Muslo derecho"),
                            gemeloIzq = gemeloIzq.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Gemelo izquierdo"),
                            gemeloDer = gemeloDer.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Gemelo derecho"),
                            bicepsIzq = bicepsIzq.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Biceps izquierdo"),
                            bicepsDer = bicepsDer.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Biceps derecho"),
                            antebrazoIzq = antebrazoIzq.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Antebrazo izquierdo"),
                            antebrazoDer = antebrazoDer.toFloatOrNull()?.takeIf { it > 0 }
                                ?: throw MedidasError.ValorNegativo("Antebrazo derecho"),
                        )
                        onSave(medida)
                        onDismiss()
                    } catch (e: MedidasError) {
                        viewModel.setError(e)
                    }
                },
                enabled = camposValidos
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}