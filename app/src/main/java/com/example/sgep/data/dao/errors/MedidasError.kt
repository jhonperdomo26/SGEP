package com.example.sgep.data.dao.errors

/**
 * Clase sellada que representa errores específicos relacionados con el módulo de medidas corporales.
 * Hereda de [Exception] y permite categorizar distintos tipos de errores de validación o de base de datos.
 */
sealed class MedidasError(message: String) : Exception(message) {

    /**
     * Se lanza cuando un campo numérico contiene un valor negativo o igual a cero.
     *
     * @param campo Nombre del campo afectado.
     */
    class ValorNegativo(val campo: String) :
        MedidasError("El campo '$campo' no puede ser negativo o cero")

    /**
     * Se lanza cuando el valor de un campo se encuentra fuera del rango esperado.
     *
     * @param campo Nombre del campo afectado.
     * @param rango Rango válido en formato de texto, por ejemplo: "30 - 150 cm".
     */
    class ValorFueraDeRango(val campo: String, val rango: String) :
        MedidasError("'$campo' fuera de rango. Debe estar entre $rango")

    /**
     * Se lanza si hay una diferencia mayor al 10% entre lados (izquierdo/derecho) en un campo específico.
     *
     * @param campo Nombre del campo con asimetría detectada.
     */
    class AsimetriaExcesiva(val campo: String) :
        MedidasError("Asimetría detectada en $campo (diferencia > 10% entre lados)")

    /**
     * Se lanza cuando alguna proporción corporal no cumple con criterios mínimos definidos.
     *
     * @param mensaje Detalle adicional sobre la proporción inválida.
     */
    class ProporcionInvalida(mensaje: String) :
        MedidasError("Proporción corporal inválida: $mensaje")

    /**
     * Se lanza si el usuario intenta acceder o modificar medidas que no le pertenecen.
     */
    class UsuarioNoCoincide :
        MedidasError("No tienes permisos para modificar estas medidas")

    /**
     * Se lanza cuando ocurre un error inesperado de base de datos.
     *
     * @param cause Excepción original lanzada por Room o el sistema.
     */
    class DatabaseError(cause: Throwable) :
        MedidasError("Error en la base de datos: ${cause.message ?: "consulta los logs"}")
}