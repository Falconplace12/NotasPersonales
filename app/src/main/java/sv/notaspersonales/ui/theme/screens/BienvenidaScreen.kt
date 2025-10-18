package sv.notaspersonales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import sv.notaspersonales.util.SharedPrefManager
import android.content.Context

@Composable
fun BienvenidaScreen(navController: NavController, context: Context) {
    val sharedPref = remember { SharedPrefManager(context) }
    var nombre by remember { mutableStateOf("") }
    var temaOscuro by remember { mutableStateOf(sharedPref.temaOscuro) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Ingrese su nombre") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tema Oscuro")
            Switch(checked = temaOscuro, onCheckedChange = {
                temaOscuro = it
                sharedPref.temaOscuro = it
            })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (nombre.isNotBlank()) {
                sharedPref.nombreUsuario = nombre
                navController.navigate("lista_notas") {
                    popUpTo("bienvenida") { inclusive = true }
                }
            }
        }) {
            Text("Continuar")
        }
    }
}
