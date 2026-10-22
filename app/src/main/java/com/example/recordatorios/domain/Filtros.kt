package com.example.recordatorios.domain

// Funciones de extensión: agregan operaciones a tipos que ya existen
// (Recordatorio, List<Recordatorio>) sin modificarlos.

fun Recordatorio.estaPendiente(): Boolean =
    estado is EstadoRecordatorio.Pendiente

fun List<Recordatorio>.pendientes(): List<Recordatorio> =
    filter { it.estaPendiente() }

fun List<Recordatorio>.completados(): List<Recordatorio> =
    filter { it.estado is EstadoRecordatorio.Completado }

fun List<Recordatorio>.conPrioridad(prioridad: Prioridad): List<Recordatorio> =
    filter { it.prioridad == prioridad }

fun List<Recordatorio>.deCategoria(categoria: Categoria): List<Recordatorio> =
    filter { it.categoria == categoria }