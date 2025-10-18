package sv.notaspersonales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sv.notaspersonales.data.local.Nota
import sv.notaspersonales.data.repository.NotaRepository

class NotaViewModel(private val repository: NotaRepository) : ViewModel() {

    val notas: StateFlow<List<Nota>> = repository.todasNotas.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun agregarNota(nota: Nota) = viewModelScope.launch {
        repository.insertar(nota)
    }

    fun actualizarNota(nota: Nota) = viewModelScope.launch {
        repository.actualizar(nota)
    }

    fun eliminarNota(nota: Nota) = viewModelScope.launch {
        repository.eliminar(nota)
    }
}
