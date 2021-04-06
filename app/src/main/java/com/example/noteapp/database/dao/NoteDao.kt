package com.example.noteapp.database.dao

import androidx.room.*
import com.example.noteapp.database.constants.Queries
import com.example.noteapp.database.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NoteDao {

    @Query(Queries.SELECT_ALL_NOTES_QUERY)
    abstract fun allNotes(): Flow<List<Note>>

    @Query(Queries.SELECT_NOTE_BY_ID)
    abstract fun getNoteById(id: Long): Flow<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun store(note: Note): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(note: Note)

    @Transaction
    open suspend fun upsert(note: Note) {
        val id: Long = store(note)
        if (id.toInt() == -1) {
            update(note)
        }
    }

    @Query(Queries.DELETE_ALL_QUERY)
    abstract suspend fun deleteAll()
}