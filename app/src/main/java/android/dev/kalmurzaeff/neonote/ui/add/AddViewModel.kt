package android.dev.kalmurzaeff.neonote.ui.add

import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import android.dev.kalmurzaeff.neonote.repository.NoteRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : KalmurzaeffDev_A
 * Date : 10/26/2023
 */

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    fun upsertNote(noteEntity: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsertNote(noteEntity)
        }
    }

}