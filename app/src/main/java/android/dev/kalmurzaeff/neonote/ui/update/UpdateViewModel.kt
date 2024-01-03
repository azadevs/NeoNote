package android.dev.kalmurzaeff.neonote.ui.update

import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import android.dev.kalmurzaeff.neonote.repository.NoteRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : KalmurzaeffDev_A
 * Date : 10/26/2023
 */

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Int =
        savedStateHandle.get<Int>("noteId") ?: error("Missing parameter {noteId}")

    private val _noteById = MutableStateFlow<NoteEntity?>(null)
    val noteById get() = _noteById

    init {
        viewModelScope.launch {
            val note = repository.getNoteById(noteId)
            _noteById.update { note }
        }
    }

    fun upsertNote(noteEntity: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsertNote(noteEntity)
        }
    }


}