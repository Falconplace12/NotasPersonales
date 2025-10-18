package sv.notaspersonales.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Nota::class], version = 1, exportSchema = false)
abstract class NotasDatabase : RoomDatabase() {
    abstract fun notaDao(): NotaDao

    companion object {
        @Volatile private var INSTANCE: NotasDatabase? = null

        fun getDatabase(context: Context): NotasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotasDatabase::class.java,
                    "notas_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
