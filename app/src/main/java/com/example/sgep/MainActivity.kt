package com.example.sgep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.sgep.ui.navigation.Navigation
import com.example.sgep.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // Configuración de navegación entre pantallas
                Navigation()
            }
        }
    }
}

// Función de previsualización
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    AppTheme {
        Navigation() // Renderizamos la navegación para la previsualización
    }
}