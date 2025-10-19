package sv.notaspersonales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import sv.notaspersonales.data.local.NotasDatabase
import sv.notaspersonales.data.repository.NotaRepository
import sv.notaspersonales.ui.screens.*
import sv.notaspersonales.util.SharedPrefManager
import sv.notaspersonales.viewmodel.NotaViewModel
import sv.notaspersonales.viewmodel.NotaViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: NotaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Inicializar base de datos y repositorio
        val dao = NotasDatabase.getDatabase(applicationContext).notaDao()
        val repository = NotaRepository(dao)

        //  Inicializar ViewModel
        viewModel = ViewModelProvider(
            this,
            NotaViewModelFactory(repository)
        )[NotaViewModel::class.java]

        //  Inicializar preferencias
        val sharedPref = SharedPrefManager(this)

        setContent {
            // Estado del tema (reactivo)
            var isDarkTheme by remember { mutableStateOf(sharedPref.temaOscuro) }

            // Controlador de navegación
            val navController = rememberNavController()

            //  Redirección automática si ya hay usuario guardado
            LaunchedEffect(Unit) {
                // Pequeña espera para mostrar la pantalla inicial suavemente
                delay(150)
                if (!sharedPref.nombreUsuario.isNullOrEmpty()) {
                    navController.navigate("lista_notas") {
                        popUpTo("bienvenida") { inclusive = true }
                    }
                }
            }

            //  Aplicar el tema dinámico global
            MaterialTheme(
                colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            ) {
                //  Definir las rutas principales de la app
                NavHost(
                    navController = navController,
                    startDestination = "bienvenida"
                ) {

                    // Pantalla de bienvenida
                    composable("bienvenida") {
                        BienvenidaScreen(
                            navController = navController,
                            context = this@MainActivity,
                            temaOscuro = isDarkTheme,
                            onTemaCambiado = { nuevoTema ->
                                isDarkTheme = nuevoTema
                                sharedPref.temaOscuro = nuevoTema
                            }
                        )
                    }

                    // Pantalla principal de notas
                    composable("lista_notas") {
                        ListaNotasScreen(
                            navController = navController,
                            viewModel = viewModel,
                            onCerrarSesion = {
                                sharedPref.nombreUsuario = null
                                navController.navigate("bienvenida") {
                                    popUpTo("lista_notas") { inclusive = true }
                                }
                            }
                        )
                    }

                    // Pantalla para crear (sin argumento)
                    composable("crear_editar") {
                        // notaExistente = null -> modo crear
                        EditarCrearNotaScreen(
                            navController = navController,
                            viewModel = viewModel,
                            notaExistente = null
                        )
                    }

                    // Pantalla para editar (con argumento notaId)
                    composable(
                        route = "crear_editar/{notaId}",
                        arguments = listOf(navArgument("notaId") { defaultValue = 0 })
                    ) { backStackEntry ->
                        val notaId = backStackEntry.arguments?.getInt("notaId") ?: 0
                        val notaExistente = viewModel.notas.collectAsState().value.find { it.id == notaId }

                        EditarCrearNotaScreen(
                            navController = navController,
                            viewModel = viewModel,
                            notaExistente = notaExistente
                        )
                    }

                    //  Pantalla de detalle de nota
                    composable(
                        "detalle/{notaId}",
                        arguments = listOf(navArgument("notaId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val notaId = backStackEntry.arguments?.getInt("notaId") ?: 0
                        DetalleNotaScreen(
                            navController = navController,
                            viewModel = viewModel,
                            notaId = notaId
                        )
                    }
                }
            }
        }
    }
}