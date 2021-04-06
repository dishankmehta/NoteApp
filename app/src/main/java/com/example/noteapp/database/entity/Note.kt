package com.example.noteapp.database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @NonNull @ColumnInfo(name = "note_title") var noteName: String?,
    @NonNull @ColumnInfo(name = "note_content") var noteContent: String?
) {
    @PrimaryKey(autoGenerate = true) var nid: Long? = null
}