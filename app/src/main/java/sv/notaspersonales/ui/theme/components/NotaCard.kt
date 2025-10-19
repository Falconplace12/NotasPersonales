package sv.notaspersonales.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sv.notaspersonales.data.local.Nota
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun NotaCard(
    nota: Nota,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    // Detectar si el tema actual es oscuro
    val isDarkTheme = !isSystemInDarkTheme()

    // Si es tema oscuro, usar texto negro para fondos claros personalizados
    val textColor = if (isDarkTheme) Color.Black else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(android.graphics.Color.parseColor(nota.color))
        ),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = nota.titulo,
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
            Text(
                text = nota.contenido,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor.copy(alpha = 0.9f)
            )
            Text(
                text = nota.categoria,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
}