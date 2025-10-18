package sv.notaspersonales.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val contenido: String,
    val categoria: String, // "Personal", "Trabajo", "Estudio"
    val fechaCreacion: String,
    val color: String
)
