package sv.notaspersonales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import sv.notaspersonales.data.local.Nota
import sv.notaspersonales.ui.components.NotaCard
import sv.notaspersonales.viewmodel.NotaViewModel

@Composable
fun ListaNotasScreen(navController: NavController, viewModel: NotaViewModel) {
    var filtroCategoria by remember { mutableStateOf("Todas") }
    var busqueda by remember { mutableStateOf("") }
    val notas by viewModel.notas.collectAsState()

    // Filtro de notas según categoría y texto de búsqueda
    val notasFiltradas = notas.filter {
        (filtroCategoria == "Todas" || it.categoria == filtroCategoria) &&
                it.titulo.contains(busqueda, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("crear_editar") }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Campo de búsqueda
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de filtro de categoría (alineados uniformemente)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val categorias = listOf("Todas", "Personal", "Trabajo", "Estudio")
                categorias.forEach { cat ->
                    val isSelected = filtroCategoria == cat
                    Button(
                        onClick = { filtroCategoria = cat },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = if (isSelected)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = cat,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de notas
            LazyColumn {
                items(notasFiltradas) { nota ->
                    NotaCard(
                        nota = nota,
                        onClick = { navController.navigate("detalle/${nota.id}") },
                        onDelete = { viewModel.eliminarNota(nota) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
