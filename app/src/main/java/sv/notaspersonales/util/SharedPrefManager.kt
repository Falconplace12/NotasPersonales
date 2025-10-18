package sv.notaspersonales.util

import android.content.Context

class SharedPrefManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var nombreUsuario: String?
        get() = prefs.getString("nombre_usuario", null)
        set(value) = prefs.edit().putString("nombre_usuario", value).apply()

    var temaOscuro: Boolean
        get() = prefs.getBoolean("tema_oscuro", false)
        set(value) = prefs.edit().putBoolean("tema_oscuro", value).apply()
}
