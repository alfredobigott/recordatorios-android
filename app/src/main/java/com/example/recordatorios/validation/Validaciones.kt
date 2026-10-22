package com.example.recordatorios.validation

import com.example.recordatorios.domain.Categoria
import com.example.recordatorios.domain.EstadoRecordatorio
import com.example.recordatorios.domain.Prioridad
import com.example.recordatorios.domain.Recordatorio
import com.example.recordatorios.domain.ResultadoCreacion
import com.example.recordatorios.domain.Ubicacion

const val LARGO_MINIMO_TITULO = 4
const val LARGO_MAXIMO_TITULO = 60
const val LARGO_MAXIMO_DESCRIPCION = 200

/** Devuelve un mensaje si el título no es válido, o null si es válido. */
fun validarTitulo(titulo: String): String? {
    if (titulo.isBlank()) {
        return "El título es obligatorio"
    }

    if (titulo.trim().length < LARGO_MINIMO_TITULO) {
        return "Ingresá al menos $LARGO_MINIMO_TITULO caracteres"
    }

    if (titulo.trim().length > LARGO_MAXIMO_TITULO) {
        return "El título no puede superar los $LARGO_MAXIMO_TITULO caracteres"
    }

    return null
}

/** La descripción es opcional: si existe, no puede superar el largo máximo. */
fun validarDescripcion(descripcion: String?): String? {
    if (descripcion != null && descripcion.length > LARGO_MAXIMO_DESCRIPCION) {
        return "La descripción no puede superar los $LARGO_MAXIMO_DESCRIPCION caracteres"
    }

    return null
}

/**
 * Único punto de entrada para crear recordatorios: si algún dato no cumple las reglas,
 * devuelve DatoInvalido y NO se construye el objeto.
 */
fun crearRecordatorio(
    id: Long,
    titulo: String,
    categoria: Categoria,
    prioridad: Prioridad,
    estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
    descripcion: String? = null,
    fechaLimite: String? = null,
    ubicacion: Ubicacion? = null
): ResultadoCreacion {
    val errorTitulo = validarTitulo(titulo)
    if (errorTitulo != null) {
        return ResultadoCreacion.DatoInvalido(campo = "titulo", mensaje = errorTitulo)
    }

    val errorDescripcion = validarDescripcion(descripcion)
    if (errorDescripcion != null) {
        return ResultadoCreacion.DatoInvalido(campo = "descripcion", mensaje = errorDescripcion)
    }

    return ResultadoCreacion.Correcto(
        Recordatorio(
            id = id,
            titulo = titulo.trim(),
            categoria = categoria,
            prioridad = prioridad,
            estado = estado,
            descripcion = descripcion,
            fechaLimite = fechaLimite,
            ubicacion = ubicacion
        )
    )
}