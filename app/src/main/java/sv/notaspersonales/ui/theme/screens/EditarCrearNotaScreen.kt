package sv.notaspersonales.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.TextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarCrearNotaScreen(
    navController: NavController,
    viewModel: NotaViewModel,
    notaExistente: Nota? = null
) {
    // Variables de estado
    var titulo by remember { mutableStateOf(notaExistente?.titulo ?: "") }
    var contenido by remember { mutableStateOf(notaExistente?.contenido ?: "") }
    var color by remember { mutableStateOf(notaExistente?.color ?: "#FFFFFF") }

    // Categoría seleccionada (editable)
    var categoriaSeleccionada by remember { mutableStateOf(notaExistente?.categoria ?: "Personal") }
    val categoriasDisponibles = listOf("Personal", "Trabajo", "Estudio")

    // Paleta de colores
    val coloresDisponibles = listOf(
        "#FFFFFF", "#FFCDD2", "#C8E6C9", "#BBDEFB", "#FFF9C4", "#E1BEE7"
    )

    // Fondo que respeta el tema global (oscuro o claro)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = if (notaExistente == null) "Crear Nueva Nota" else "Editar Nota",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo de título
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de contenido
            OutlinedTextField(
                value = contenido,
                onValueChange = { contenido = it },
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección "Categoría"
            Text(
                "Categoría:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoriasDisponibles.forEach { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = { categoriaSeleccionada = categoria },
                        label = { Text(categoria) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            selectedLabelColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección "Color de nota"
            Text(
                "Color de nota:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                coloresDisponibles.forEach { c ->
                    val colorSeleccionado = color == c
                    val colorParsed = Color(android.graphics.Color.parseColor(c))

                    val bordeAnimado by animateDpAsState(
                        targetValue = if (colorSeleccionado) 4.dp else 0.dp,
                        animationSpec = tween(durationMillis = 250)
                    )

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                width = bordeAnimado,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .background(colorParsed, shape = CircleShape)
                            .clickable { color = c }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Guardar / Crear
            Button(
                onClick = {
                    val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                    val nuevaNota = Nota(
                        id = notaExistente?.id ?: 0,
                        titulo = titulo,
                        contenido = contenido,
                        categoria = categoriaSeleccionada,
                        fechaCreacion = fecha,
                        color = color
                    )

                    try {
                        if (notaExistente == null) {
                            viewModel.agregarNota(nuevaNota)
                        } else {
                            viewModel.actualizarNota(nuevaNota)
                        }

                        // ✅ Volver directamente a la lista de notas
                        navController.navigate("lista_notas") {
                            popUpTo("lista_notas") { inclusive = true }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    if (notaExistente == null) "Crear Nota" else "Guardar Cambios",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}