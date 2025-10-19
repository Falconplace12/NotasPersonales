package sv.notaspersonales.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import sv.notaspersonales.viewmodel.NotaViewModel
import androidx.compose.animation.core.animateFloatAsState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleNotaScreen(
    navController: NavController,
    viewModel: NotaViewModel,
    notaId: Int
) {
    // Obtener la lista de notas
    val notasState by viewModel.notas.collectAsState()
    val nota = notasState.find { it.id == notaId }

    // Estado para mostrar el diálogo de confirmación
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Surface que respeta el tema global
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Si no hay nota, mostrar mensaje
        if (nota == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nota no encontrada",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            return@Surface
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Tarjeta de detalle
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = nota.titulo,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = nota.contenido,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Categoría: ${nota.categoria}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Fecha: ${nota.fechaCreacion}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones de acción (Editar y Eliminar)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ---- BOTÓN EDITAR ----
                val editInteraction = remember { MutableInteractionSource() }
                val editPressed by editInteraction.collectIsPressedAsState()
                val editScale by animateFloatAsState(if (editPressed) 1.05f else 1f)

                OutlinedButton(
                    onClick = {
                        navController.navigate("crear_editar/${nota.id}")
                    },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .height(44.dp)
                        .weight(1f)
                        .scale(editScale),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    interactionSource = editInteraction
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Editar")
                }

                // ---- BOTÓN ELIMINAR ----
                val deleteInteraction = remember { MutableInteractionSource() }
                val deletePressed by deleteInteraction.collectIsPressedAsState()
                val deleteScale by animateFloatAsState(if (deletePressed) 1.05f else 1f)

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .height(44.dp)
                        .weight(1f)
                        .scale(deleteScale),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    interactionSource = deleteInteraction
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eliminar")
                }
            }
        }
    }

    //  Diálogo de confirmación de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar nota") },
            text = {
                Text(
                    "¿Estás segura de que deseas eliminar esta nota? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    nota?.let {
                        viewModel.eliminarNota(it)
                    }
                    showDeleteDialog = false

                    // Pequeño delay visual antes de volver
                    navController.navigate("lista_notas") {
                        popUpTo("lista_notas") { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}