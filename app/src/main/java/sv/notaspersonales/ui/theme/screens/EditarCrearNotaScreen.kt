package sv.notaspersonales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import sv.notaspersonales.data.local.Nota
import sv.notaspersonales.viewmodel.NotaViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditarCrearNotaScreen(
    navController: NavController,
    viewModel: NotaViewModel,
    notaExistente: Nota? = null
) {
    var titulo by remember { mutableStateOf(notaExistente?.titulo ?: "") }
    var contenido by remember { mutableStateOf(notaExistente?.contenido ?: "") }
    var categoria by remember { mutableStateOf(notaExistente?.categoria ?: "Personal") }
    var color by remember { mutableStateOf(notaExistente?.color ?: "#FFFFFF") }

    val coloresDisponibles = listOf("#FFFFFF", "#FFCDD2", "#C8E6C9", "#BBDEFB", "#FFF9C4")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            listOf("Personal", "Trabajo", "Estudio").forEach { cat ->
                Button(
                    onClick = { categoria = cat },
                    modifier = Modifier.padding(end = 8.dp)
                ) { Text(cat) }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            coloresDisponibles.forEach { c ->
                Button(
                    onClick = { color = c },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor(c))),
                    modifier = Modifier.padding(end = 8.dp)
                ) {}
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            val nuevaNota = Nota(
                id = notaExistente?.id ?: 0,
                titulo = titulo,
                contenido = contenido,
                categoria = categoria,
                fechaCreacion = fecha,
                color = color
            )
            if (notaExistente == null) viewModel.agregarNota(nuevaNota)
            else viewModel.actualizarNota(nuevaNota)
            navController.popBackStack()
        }) {
            Text(if (notaExistente == null) "Crear Nota" else "Guardar Cambios")
        }
    }
}
