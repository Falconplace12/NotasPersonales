package sv.notaspersonales.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import sv.notaspersonales.util.SharedPrefManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BienvenidaScreen(
    navController: NavController,
    context: Context,
    temaOscuro: Boolean,
    onTemaCambiado: (Boolean) -> Unit
) {
    val sharedPref = remember { SharedPrefManager(context) }
    var nombre by remember { mutableStateOf(sharedPref.nombreUsuario ?: "") }
    var showContent by remember { mutableStateOf(false) }

    // Animación inicial
    LaunchedEffect(Unit) {
        delay(200)
        showContent = true
    }

    // Fondo dinámico que se adapta al tema
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(600)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ícono superior
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Bienvenida",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 12.dp)
                        )

                        // Título
                        Text(
                            text = "¡Bienvenido!",
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Descripción
                        Text(
                            text = "Organiza tus notas personales fácilmente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.alpha(0.9f),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // Campo de texto
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Ingrese su nombre") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Switch para tema oscuro
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Tema Oscuro",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = temaOscuro,
                                onCheckedChange = onTemaCambiado,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botón "Continuar"
                        Button(
                            onClick = {
                                if (nombre.isNotBlank()) {
                                    sharedPref.nombreUsuario = nombre
                                    navController.navigate("lista_notas") {
                                        popUpTo("bienvenida") { inclusive = true }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(50.dp)
                        ) {
                            Text("Continuar", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
