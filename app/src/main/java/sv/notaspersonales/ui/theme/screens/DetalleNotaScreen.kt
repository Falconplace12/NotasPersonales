package sv.notaspersonales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import sv.notaspersonales.data.local.Nota
import sv.notaspersonales.viewmodel.NotaViewModel

@Composable
fun DetalleNotaScreen(navController: NavController, viewModel: NotaViewModel, notaId: Int) {
    val nota = viewModel.notas.collectAsState().value.find { it.id == notaId }

    nota?.let {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Text(it.titulo, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(it.contenido)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Categoría: ${it.categoria}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Fecha: ${it.fechaCreacion}")
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = {
                    navController.navigate("crear_editar/${it.id}")
                }, modifier = Modifier.padding(end = 8.dp)) {
                    Text("Editar")
                }
                Button(onClick = {
                    viewModel.eliminarNota(it)
                    navController.popBackStack()
                }) {
                    Text("Eliminar")
                }
            }
        }
    }
}
