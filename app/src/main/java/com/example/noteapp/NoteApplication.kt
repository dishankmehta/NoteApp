package com.example.noteapp

import android.app.Application
import com.example.noteapp.database.AppDatabase
import com.example.noteapp.database.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NoteApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { NoteRepository.getInstance(database) }

}