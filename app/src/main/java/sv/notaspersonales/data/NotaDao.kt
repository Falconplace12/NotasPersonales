package sv.notaspersonales.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDao {
    @Query("SELECT * FROM notas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE categoria = :cat")
    fun obtenerPorCategoria(cat: String): Flow<List<Nota>>

   /** insercion de notas*/
    @Insert
    suspend fun insertar(nota: Nota)
    /** actualizando notas*/
    @Update
    suspend fun actualizar(nota: Nota)
    /** eliminando notas*/
    @Delete
    suspend fun eliminar(nota: Nota)
}
