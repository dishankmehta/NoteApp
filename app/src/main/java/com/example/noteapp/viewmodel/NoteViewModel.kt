package com.example.noteapp.viewmodel

import android.app.Application
import com.example.noteapp.database.entity.Note
import com.example.noteapp.database.NoteRepository

import androidx.lifecycle.*
import com.example.noteapp.NoteApplication
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class NoteViewModel(private val repository: NoteRepository): ViewModel() {

    val allNotes: LiveData<List<Note>> = repository.mNotes.asLiveData()

    fun store(note: Note) = viewModelScope.launch {
        repository.store(note)
    }

    fun upsert(note: Note) = viewModelScope.launch {
        repository.upsert(note)
    }

    fun getNoteById(id: Long): LiveData<Note> {
        return repository.getNoteById(id).asLiveData()
    }

    class NoteViewModelFactory(private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return (application as NoteApplication).repository?.let { NoteViewModel(it) } as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}


