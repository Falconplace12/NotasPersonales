package sv.notaspersonales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import sv.notaspersonales.viewmodel.NotaViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import sv.notaspersonales.data.local.Nota

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaNotasScreen(
    navController: NavController,
    viewModel: NotaViewModel,
    onCerrarSesion: () -> Unit
) {
    var filtroCategoria by remember { mutableStateOf("Todas") }
    var busqueda by remember { mutableStateOf("") }
    val notas by viewModel.notas.collectAsState()

    // Filtro dinámico según categoría y texto
    val notasFiltradas = notas.filter {
        (filtroCategoria == "Todas" || it.categoria == filtroCategoria) &&
                it.titulo.contains(busqueda, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Notas") },
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("crear_editar") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar nota")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // CONTADOR DE NOTAS
            Text(
                text = "Total de notas: ${notas.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Campo de búsqueda
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por título") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Categorías con chips
            val categorias = listOf("Todas", "Personal", "Trabajo", "Estudio")

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { categoria ->
                    val isSelected = filtroCategoria == categoria

                    FilterChip(
                        selected = isSelected,
                        onClick = { filtroCategoria = categoria },
                        label = {
                            Text(
                                categoria,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        shape = RoundedCornerShape(25.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                            selectedLabelColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LISTA DE NOTAS
            LazyColumn {
                items(notasFiltradas) { nota ->

                    // ✅ Color guardado por el usuario o color de respaldo
                    val backgroundColor = try {
                        Color(android.graphics.Color.parseColor(nota.color))
                    } catch (e: Exception) {
                        Color(0xFFF5F5F5) // color por defecto si el formato no es válido
                    }

                    // ✅ Tarjeta con color personalizado
                    Card(
                        onClick = { navController.navigate("detalle/${nota.id}") },
                        colors = CardDefaults.cardColors(containerColor = backgroundColor),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = nota.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (nota.contenido.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = nota.contenido,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.DarkGray,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = nota.categoria,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Mensaje si no hay notas
                if (notasFiltradas.isEmpty()) {
                    item {
                        Text(
                            "No hay notas disponibles",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

