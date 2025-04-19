package com.example.sgep.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgep.data.database.AppDatabase
import com.example.sgep.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private val _loginResult = MutableStateFlow("")
    val loginResult: StateFlow<String> = _loginResult

    init {
        val db = AppDatabase.getDatabase(application)
        repository = UserRepository(db.userDao())
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = repository.login(email, password)
            _loginResult.value = if (user != null) {
                "Inicio de sesión exitoso"
            } else {
                "Credenciales incorrectas"
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            repository.registerUser(email, password)
            _loginResult.value = "Usuario registrado"
        }
    }
}
