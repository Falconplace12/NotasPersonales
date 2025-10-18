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

@Composable
fun NotaCard(
    nota: Nota,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(android.graphics.Color.parseColor(nota.color))
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(nota.titulo, style = MaterialTheme.typography.titleMedium)
            Text(nota.contenido.take(50) + "...")
            Text(nota.categoria, style = MaterialTheme.typography.labelSmall)
        }
    }
}
