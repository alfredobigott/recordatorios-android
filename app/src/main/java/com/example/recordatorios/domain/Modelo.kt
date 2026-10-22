package com.example.recordatorios.domain

/** Categoría de un recordatorio. Dos con el mismo nombre son iguales (==). */
data class Categoria(val nombre: String)

/** Lugar opcional asociado a un recordatorio. */
data class Ubicacion(
    val nombre: String,
    val latitud: Double,
    val longitud: Double
)

/** Prioridad: una lista cerrada con tres opciones. */
sealed class Prioridad(val etiqueta: String, val nivel: Int) {
    data object Baja : Prioridad("Baja", 1)
    data object Media : Prioridad("Media", 2)
    data object Alta : Prioridad("Alta", 3)
}

/** Estado: una lista cerrada de cuatro opciones; cada una guarda solo lo que necesita. */
sealed class EstadoRecordatorio {
    data object Pendiente : EstadoRecordatorio()
    data class Completado(val fechaCompletado: String) : EstadoRecordatorio()
    data class Pospuesto(val nuevaFecha: String) : EstadoRecordatorio()
    data class Cancelado(val motivo: String? = null) : EstadoRecordatorio()
}

/** Un recordatorio: primero lo obligatorio, al final lo que tiene valor por defecto. */
data class Recordatorio(
    val id: Long,
    val titulo: String,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
    val descripcion: String? = null,
    val fechaLimite: String? = null,
    val ubicacion: Ubicacion? = null
)

sealed class ResultadoCreacion {
    data class Correcto(val recordatorio: Recordatorio) : ResultadoCreacion()
    data class DatoInvalido(val campo: String, val mensaje: String) : ResultadoCreacion()
}