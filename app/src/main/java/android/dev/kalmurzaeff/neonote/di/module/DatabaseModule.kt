package android.dev.kalmurzaeff.neonote.di.module

import android.content.Context
import android.dev.kalmurzaeff.neonote.data.local.NoteDatabase
import android.dev.kalmurzaeff.neonote.data.local.dao.NoteDao
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by : KalmurzaeffDev_A
 * Date : 10/23/2023
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(context, NoteDatabase::class.java, "note_database").build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }

}