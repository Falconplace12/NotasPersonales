package sv.notaspersonales.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa una nota dentro de la base de datos local de la aplicación.
 *
 * Cada objeto de esta clase corresponde a un registro en la tabla `notas` de la base de datos Room.
 * Contiene la información esencial de una nota creada por el usuario, incluyendo título, contenido,
 * categoría, fecha de creación y color de identificación.
 *
 * @property id Identificador único de la nota. Se genera automáticamente al insertar un nuevo registro.
 * @property titulo Título breve de la nota.
 * @property contenido Texto principal o cuerpo de la nota.
 * @property categoria Categoría asignada a la nota (por ejemplo: "Personal", "Trabajo", "Estudio").
 * @property fechaCreacion Fecha en la que se creó la nota, almacenada como cadena de texto.
 * @property color Color asociado visualmente a la nota (por ejemplo: un código hexadecimal).
 */
@Entity(tableName = "notas")
/**
 * Entidad de Room que representa la tabla 'notas' en la base de datos local.
 */
data class Nota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val contenido: String,
    val categoria: String, // "Personal", "Trabajo", "Estudio"
    val fechaCreacion: String,
    val color: String
)
