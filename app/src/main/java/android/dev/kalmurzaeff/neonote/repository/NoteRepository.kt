package android.dev.kalmurzaeff.neonote.repository

import android.dev.kalmurzaeff.neonote.data.local.dao.NoteDao
import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by : KalmurzaeffDev_A
 * Date : 10/24/2023
 */

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {

    suspend fun upsertNote(noteEntity: NoteEntity) {
        noteDao.upsertNote(noteEntity)
    }

    suspend fun searchByTitle(searchQuery: String): List<NoteEntity> {
        return noteDao.searchByTitle(searchQuery)
    }

    suspend fun deleteNote(noteEntity: NoteEntity) {
        noteDao.deleteNote(noteEntity)
    }

    fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    suspend fun getNoteById(noteId: Int): NoteEntity {
        return noteDao.getNoteById(noteId)
    }

    fun getSavedNotes(): Flow<List<NoteEntity>> {
        return noteDao.getSavedNotes()
    }

}