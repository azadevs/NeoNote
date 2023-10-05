package android.dev.kalmurzaeff.notesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteEntity(
    val title: String,
    val description: String,
    val imageUri: String? = null,
    val colorPosition: Int,
    val date: Long,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var isSave: Boolean = false,
)
