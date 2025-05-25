package com.example.sgep.data.dao.errors

sealed class MedidasError(message: String) : Exception(message) {
    // -----------------------------------------------
    // Errores de Validación (400 Bad Request)
    // -----------------------------------------------
    class ValorNegativo(val campo: String) :
        MedidasError("El campo '$campo' no puede ser negativo o cero")

    class ValorFueraDeRango(val campo: String, val rango: String) :
        MedidasError("'$campo' fuera de rango. Debe estar entre $rango")

    class AsimetriaExcesiva(val campo: String) :
        MedidasError("Asimetría detectada en $campo (diferencia > 10% entre lados)")

    class CamposIncompletos(val camposFaltantes: List<String>) :
        MedidasError("Campos requeridos faltantes: ${camposFaltantes.joinToString(", ")}")

    // -----------------------------------------------
    // Errores de Autorización (403 Forbidden)
    // -----------------------------------------------
    class UsuarioNoCoincide :
        MedidasError("No tienes permisos para modificar estas medidas")

    class SesionExpirada :
        MedidasError("La sesión ha expirado. Vuelve a iniciar sesión")

    class MedidaDuplicada(message: String) : MedidasError(message)

    // -----------------------------------------------
    // Errores de Recursos (404 Not Found)
    // -----------------------------------------------
    class MedidaNoEncontrada :
        MedidasError("No se encontró el registro de medidas solicitado")

    class UsuarioNoEncontrado :
        MedidasError("El usuario asociado no existe")

    // -----------------------------------------------
    // Errores del Sistema (500 Server Error)
    // -----------------------------------------------
    class DatabaseError(cause: Throwable) :
        MedidasError("Error en la base de datos: ${cause.message ?: "consulta los logs"}")

    class ErrorDesconocido(cause: Throwable) :
        MedidasError("Error inesperado: ${cause.javaClass.simpleName}")
}

// Extensión útil para convertir cualquier excepción en MedidasError
fun Exception.toMedidasError(): MedidasError = when (this) {
    is MedidasError -> this
    else -> MedidasError.ErrorDesconocido(this)
}