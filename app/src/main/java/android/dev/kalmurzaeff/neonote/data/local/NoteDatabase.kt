package android.dev.kalmurzaeff.neonote.data.local

import android.dev.kalmurzaeff.neonote.data.local.dao.NoteDao
import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 2, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {


    abstract fun noteDao(): NoteDao

//    companion object {
//        @Volatile
//        var INSTANCE: NoteDatabase? = null
//
//        fun getInstance(context: Context): NoteDatabase =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE
//                    ?: buildDatabase(context).also {
//                        INSTANCE = it
//                    }
//            }
//
//        private fun buildDatabase(context: Context) =
//            Room.databaseBuilder(context, NoteDatabase::class.java, "note_database")
//                .allowMainThreadQueries().build()
//    }
}
