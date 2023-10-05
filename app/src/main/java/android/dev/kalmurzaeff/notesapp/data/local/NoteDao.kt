package android.dev.kalmurzaeff.notesapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NoteDao {

    @Upsert
    fun upsertNote(noteEntity: NoteEntity)

    @Query("Select * from note_table")
    fun getAllNotes(): MutableList<NoteEntity>

    @Query("Select * from note_table Where id==:noteId")
    fun getNoteById(noteId: Int): NoteEntity

    @Query("Select * from note_table Where title Like :searchQuery")
    fun searchByTitle(searchQuery: String): List<NoteEntity>

    @Delete
    fun deleteNote(noteEntity: NoteEntity)

    @Query("Select * from note_table Where isSave==1")
    fun getSavedNotes(): List<NoteEntity>

}