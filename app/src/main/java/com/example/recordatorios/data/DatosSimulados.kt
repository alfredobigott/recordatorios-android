package com.example.recordatorios.data

import com.example.recordatorios.domain.Categoria
import com.example.recordatorios.domain.EstadoRecordatorio
import com.example.recordatorios.domain.Prioridad
import com.example.recordatorios.domain.Recordatorio
import com.example.recordatorios.domain.ResultadoCreacion
import com.example.recordatorios.domain.Ubicacion
import com.example.recordatorios.validation.crearRecordatorio

private val trabajo = Categoria("Trabajo")
private val personal = Categoria("Personal")
private val salud = Categoria("Salud")
private val estudio = Categoria("Estudio")

// SET: "Trabajo" está repetida a propósito. Como Categoria es una data class,
// el Set reconoce que es la misma y guarda una sola: quedan 4 categorías.
val categoriasSimuladas: Set<Categoria> =
    setOf(trabajo, personal, salud, estudio, Categoria("Trabajo"))

// MAP: cada categoría (clave) se asocia con su etiqueta visible (valor).
val etiquetasSimuladas: Map<Categoria, String> = mapOf(
    trabajo to "💼 Trabajo",
    personal to "🏠 Personal",
    salud to "🏥 Salud",
    estudio to "📚 Estudio"
)

// LIST: conserva el orden y permite repetidos. Cada recordatorio pasa por la validación.
private val resultadosDeCreacion: List<ResultadoCreacion> = listOf(
    crearRecordatorio(
        id = 1,
        titulo = "Entregar el informe mensual",
        categoria = trabajo,
        prioridad = Prioridad.Alta,
        descripcion = "Enviarlo a coordinación antes de las 18 h",
        fechaLimite = "2026-10-20"
    ),
    crearRecordatorio(
        id = 2,
        titulo = "Reunión de equipo",
        categoria = trabajo,
        prioridad = Prioridad.Media,
        estado = EstadoRecordatorio.Completado(fechaCompletado = "2026-10-14"),
        ubicacion = Ubicacion("Oficina central", -34.6037, -58.3816)
    ),
    crearRecordatorio(
        id = 3,
        titulo = "Comprar verduras",
        categoria = personal,
        prioridad = Prioridad.Baja,
        ubicacion = Ubicacion("Feria del barrio", -34.6283, -58.4701)
    ),
    crearRecordatorio(
        id = 4,
        titulo = "Turno con el odontólogo",
        categoria = salud,
        prioridad = Prioridad.Alta,
        estado = EstadoRecordatorio.Pospuesto(nuevaFecha = "2026-11-03")
    ),
    crearRecordatorio(
        id = 5,
        titulo = "Repasar sealed classes",
        categoria = estudio,
        prioridad = Prioridad.Media,
        descripcion = "Tema 2 de la Unidad 2",
        fechaLimite = "2026-10-22"
    ),
    crearRecordatorio(
        id = 6,
        titulo = "Renovar el abono de transporte",
        categoria = personal,
        prioridad = Prioridad.Baja,
        estado = EstadoRecordatorio.Cancelado(motivo = "Ya no hace falta")
    ),
    crearRecordatorio(
        id = 7,
        titulo = "Preparar la presentación",
        categoria = trabajo,
        prioridad = Prioridad.Alta,
        fechaLimite = "2026-10-25"
    ),
    // Inválido a propósito: el título tiene menos de 4 caracteres, la validación lo rechaza.
    crearRecordatorio(
        id = 8,
        titulo = "ok",
        categoria = estudio,
        prioridad = Prioridad.Media
    )
)

// A la pantalla solo llegan los recordatorios que superaron la validación.
val recordatoriosSimulados: List<Recordatorio> =
    resultadosDeCreacion
        .filterIsInstance<ResultadoCreacion.Correcto>()
        .map { it.recordatorio }

// Los rechazados se conservan aparte, con el motivo, para poder informarlos.
val rechazadosSimulados: List<ResultadoCreacion.DatoInvalido> =
    resultadosDeCreacion
        .filterIsInstance<ResultadoCreacion.DatoInvalido>()