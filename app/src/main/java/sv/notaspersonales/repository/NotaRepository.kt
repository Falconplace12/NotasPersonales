package sv.notaspersonales.data.repository

import sv.notaspersonales.data.local.Nota
import sv.notaspersonales.data.local.NotaDao
import kotlinx.coroutines.flow.Flow

class NotaRepository(private val dao: NotaDao) {

    val todasNotas: Flow<List<Nota>> = dao.obtenerTodas()

    fun notasPorCategoria(categoria: String): Flow<List<Nota>> = dao.obtenerPorCategoria(categoria)

    suspend fun insertar(nota: Nota) = dao.insertar(nota)
    suspend fun actualizar(nota: Nota) = dao.actualizar(nota)
    suspend fun eliminar(nota: Nota) = dao.eliminar(nota)
}
