package android.dev.kalmurzaeff.neonote.ui.list

import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import android.dev.kalmurzaeff.neonote.repository.NoteRepository
import android.dev.kalmurzaeff.neonote.utils.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : KalmurzaeffDev_A
 * Date : 10/26/2023
 */

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<Resource<List<NoteEntity>>>(Resource.Loading)
    val notes = _notes.asStateFlow()

    private val _searchingNotes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val searchingNotes get() = _searchingNotes

    fun upsertNote(noteEntity: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsertNote(noteEntity)
        }
    }


      fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllNotes()
                .catch {
                    _notes.value = Resource.Error(it.localizedMessage ?: "unknown error")
                }
                .onStart {
                    _notes.value = Resource.Loading
                }.collect {
                    _notes.value = Resource.Success(it)
                }
        }
    }

    fun searchByTitle(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = repository.searchByTitle(query)
            _searchingNotes.value = notes
        }
    }

    fun deleteNote(noteEntity: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(noteEntity)
        }
    }
}