package android.dev.kalmurzaeff.neonote.data.local

import android.dev.kalmurzaeff.neonote.data.local.dao.NoteDao
import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

}
