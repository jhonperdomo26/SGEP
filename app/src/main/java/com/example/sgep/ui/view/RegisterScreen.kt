package com.example.sgep.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sgep.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: LoginViewModel,
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentWeight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }

    val registerMessage by viewModel.registerResult.collectAsStateWithLifecycle()

    LaunchedEffect(registerMessage) {
        if (registerMessage == "Registro exitoso") {
            delay(1000)
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Fondo degradado igual que en login
        Box(
            modifier = Modifier
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
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()), // Scroll para formularios largos
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            // Header consistente
//            Image(
//                painter = painterResource(id = R.drawable.ic_gym_logo),
//                contentDescription = "App Logo",
//                modifier = Modifier
//                    .size(100.dp)
//                    .padding(bottom = 24.dp),
//                contentScale = ContentScale.Fit
//            )

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (registerMessage.isNotEmpty()) {
                Text(
                    text = registerMessage,
                    color = if (registerMessage.contains("exitoso", ignoreCase = true))
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Campos del formulario con estilo consistente
            InputField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Nombre Completo",
                keyboardType = KeyboardType.Text
            )

            InputField(
                value = email,
                onValueChange = { email = it },
                label = "Correo Electrónico",
                keyboardType = KeyboardType.Email
            )

            PasswordField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña"
            )

            PasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar Contraseña"
            )

            InputField(
                value = currentWeight,
                onValueChange = { currentWeight = it },
                label = "Peso Actual (kg)",
                keyboardType = KeyboardType.Number,
                isOptional = true
            )

            InputField(
                value = height,
                onValueChange = { height = it },
                label = "Estatura (cm)",
                keyboardType = KeyboardType.Number,
                isOptional = true
            )

            InputField(
                value = goal,
                onValueChange = { goal = it },
                label = "Objetivo Personal",
                keyboardType = KeyboardType.Text,
                isOptional = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (password != confirmPassword) {
                        viewModel.updateRegisterErrorMessage("Las contraseñas no coinciden")
                    } else {
                        val weight = currentWeight.toDoubleOrNull()
                        val heightValue = height.toDoubleOrNull()
                        viewModel.register(
                            nombre = fullName,
                            email = email,
                            password = password,
                            pesoActual = weight,
                            estatura = heightValue,
                            objetivo = goal
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "¿Ya tienes cuenta? Inicia Sesión",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Componente reutilizable para campos de texto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    isOptional: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = if (isOptional) "$label (opcional)" else label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Componente reutilizable para campos de contraseña
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle password visibility",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}