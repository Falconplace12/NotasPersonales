package sv.notaspersonales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.*
import sv.notaspersonales.data.local.NotasDatabase
import sv.notaspersonales.data.repository.NotaRepository
import sv.notaspersonales.ui.screens.*
import sv.notaspersonales.viewmodel.NotaViewModel
import sv.notaspersonales.viewmodel.NotaViewModelFactory
import sv.notaspersonales.util.SharedPrefManager
import androidx.navigation.navArgument
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: NotaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Database y Repositorio
        val dao = NotasDatabase.getDatabase(applicationContext).notaDao()
        val repository = NotaRepository(dao)

        // Inicializar ViewModel
        viewModel = ViewModelProvider(
            this,
            NotaViewModelFactory(repository)
        )[NotaViewModel::class.java]

        val sharedPref = SharedPrefManager(this)

        setContent {
            MaterialTheme(
                colorScheme = if (sharedPref.temaOscuro) darkColorScheme() else lightColorScheme()
            ) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination =
                    if (sharedPref.nombreUsuario.isNullOrEmpty()) "bienvenida" else "lista_notas"
                ) {
                    composable("bienvenida") {
                        BienvenidaScreen(navController = navController, context = this@MainActivity)
                    }
                    composable("lista_notas") {
                        ListaNotasScreen(navController = navController, viewModel = viewModel)
                    }
                    composable(
                        "crear_editar",
                    ) {
                        EditarCrearNotaScreen(navController = navController, viewModel = viewModel)
                    }
                    composable(
                        "crear_editar/{notaId}",
                        arguments = listOf(navArgument("notaId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val notaId = backStackEntry.arguments?.getInt("notaId") ?: 0
                        val notaExistente = viewModel.notas.collectAsState().value.find { it.id == notaId }
                        EditarCrearNotaScreen(
                            navController = navController,
                            viewModel = viewModel,
                            notaExistente = notaExistente
                        )
                    }
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
