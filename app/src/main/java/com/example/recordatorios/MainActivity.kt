package com.example.recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.recordatorios.data.categoriasSimuladas
import com.example.recordatorios.data.etiquetasSimuladas
import com.example.recordatorios.data.rechazadosSimulados
import com.example.recordatorios.data.recordatoriosSimulados
import com.example.recordatorios.ui.PantallaRecordatorios
import com.example.recordatorios.ui.theme.RecordatoriosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecordatoriosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaRecordatorios(
                        recordatorios = recordatoriosSimulados,
                        categorias = categoriasSimuladas,
                        etiquetas = etiquetasSimuladas,
                        cantidadDescartados = rechazadosSimulados.size,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}