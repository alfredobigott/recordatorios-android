package com.example.recordatorios.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recordatorios.data.categoriasSimuladas
import com.example.recordatorios.data.etiquetasSimuladas
import com.example.recordatorios.data.rechazadosSimulados
import com.example.recordatorios.data.recordatoriosSimulados
import com.example.recordatorios.domain.Categoria
import com.example.recordatorios.domain.EstadoRecordatorio
import com.example.recordatorios.domain.Prioridad
import com.example.recordatorios.domain.Recordatorio
import com.example.recordatorios.domain.completados
import com.example.recordatorios.domain.conPrioridad
import com.example.recordatorios.domain.pendientes
import com.example.recordatorios.ui.theme.RecordatoriosTheme

/** Pantalla inicial: encabezado con un resumen y la lista de recordatorios. */
@Composable
fun PantallaRecordatorios(
    recordatorios: List<Recordatorio>,
    categorias: Set<Categoria>,
    etiquetas: Map<Categoria, String>,
    modifier: Modifier = Modifier,
    cantidadDescartados: Int = 0
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Encabezado(
                recordatorios = recordatorios,
                categorias = categorias,
                etiquetas = etiquetas,
                cantidadDescartados = cantidadDescartados
            )
        }

        items(recordatorios, key = { it.id }) { recordatorio ->
            TarjetaRecordatorio(
                recordatorio = recordatorio,
                etiquetaCategoria = etiquetas[recordatorio.categoria]
                    ?: recordatorio.categoria.nombre
            )
        }
    }
}

@Composable
private fun Encabezado(
    recordatorios: List<Recordatorio>,
    categorias: Set<Categoria>,
    etiquetas: Map<Categoria, String>,
    cantidadDescartados: Int
) {
    val cantidadPendientes = recordatorios.pendientes().size
    val cantidadCompletados = recordatorios.completados().size
    val cantidadAlta = recordatorios.conPrioridad(Prioridad.Alta).size
    val textoCategorias = categorias.joinToString(separator = "   ") { categoria ->
        etiquetas[categoria] ?: categoria.nombre
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Mis recordatorios",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Pendientes: $cantidadPendientes  ·  " +
                            "Completados: $cantidadCompletados  ·  " +
                            "Prioridad alta: $cantidadAlta",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = textoCategorias,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (cantidadDescartados > 0) {
                    Text(
                        text = "Descartados por la validación: $cantidadDescartados",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaRecordatorio(
    recordatorio: Recordatorio,
    etiquetaCategoria: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recordatorio.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                EtiquetaPrioridad(prioridad = recordatorio.prioridad)
            }

            Text(
                text = "Categoría: $etiquetaCategoria",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Estado: ${recordatorio.estado.textoVisible()}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Datos opcionales: solo se dibujan si existen (?.let no se ejecuta con null).
            recordatorio.descripcion?.let { descripcion ->
                Text(text = descripcion, style = MaterialTheme.typography.bodySmall)
            }
            recordatorio.fechaLimite?.let { fecha ->
                Text(text = "Vence: $fecha", style = MaterialTheme.typography.bodySmall)
            }
            recordatorio.ubicacion?.let { ubicacion ->
                Text(text = "📍 ${ubicacion.nombre}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun EtiquetaPrioridad(
    prioridad: Prioridad,
    modifier: Modifier = Modifier
) {
    // when exhaustivo sobre una sealed class: no hace falta `else`.
    val colorFondo = when (prioridad) {
        Prioridad.Alta -> MaterialTheme.colorScheme.errorContainer
        Prioridad.Media -> MaterialTheme.colorScheme.tertiaryContainer
        Prioridad.Baja -> MaterialTheme.colorScheme.secondaryContainer
    }
    val colorTexto = when (prioridad) {
        Prioridad.Alta -> MaterialTheme.colorScheme.onErrorContainer
        Prioridad.Media -> MaterialTheme.colorScheme.onTertiaryContainer
        Prioridad.Baja -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Surface(
        modifier = modifier,
        color = colorFondo,
        contentColor = colorTexto,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = prioridad.etiqueta,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/** Texto que se muestra para cada estado. Cada rama usa solo los datos de su variante. */
private fun EstadoRecordatorio.textoVisible(): String =
    when (this) {
        is EstadoRecordatorio.Pendiente ->
            "Pendiente"

        is EstadoRecordatorio.Completado ->
            "Completado el $fechaCompletado"

        is EstadoRecordatorio.Pospuesto ->
            "Pospuesto para el $nuevaFecha"

        is EstadoRecordatorio.Cancelado ->
            if (motivo != null) "Cancelado ($motivo)" else "Cancelado"
    }

@Preview(showBackground = true)
@Composable
fun PantallaRecordatoriosPreview() {
    RecordatoriosTheme {
        PantallaRecordatorios(
            recordatorios = recordatoriosSimulados,
            categorias = categoriasSimuladas,
            etiquetas = etiquetasSimuladas,
            cantidadDescartados = rechazadosSimulados.size
        )
    }
}