package com.example.noteapp.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.noteapp.database.entity.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(appDatabase: AppDatabase) {


    val mNotes: Flow<List<Note>>
    private var mAppDatabase: AppDatabase = appDatabase

    init {
        mNotes = mAppDatabase.noteDao().allNotes()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun store(note: Note) {
        mAppDatabase.noteDao().store(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun upsert(note: Note) {
        mAppDatabase.noteDao().upsert(note)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getNoteById(id: Long): Flow<Note> {
        return mAppDatabase.noteDao().getNoteById(id)
    }

    companion object {

        @Volatile
        private var INSTANCE: NoteRepository? = null

        fun getInstance(appDatabase: AppDatabase): NoteRepository? {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            NoteRepository(
                                appDatabase
                            )
                    }
                }
            }
            return INSTANCE
        }
    }

}