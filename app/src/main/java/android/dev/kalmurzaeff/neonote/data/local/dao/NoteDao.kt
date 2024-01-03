package android.dev.kalmurzaeff.neonote.data.local.dao

import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(noteEntity: NoteEntity)

    @Query("Select * from note_table")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("Select * from note_table Where id==:noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity

    @Query("Select * from note_table Where title Like :searchQuery")
    suspend fun searchByTitle(searchQuery: String): List<NoteEntity>

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    @Query("Select * from note_table Where isSave==1")
    fun getSavedNotes(): Flow<List<NoteEntity>>

}