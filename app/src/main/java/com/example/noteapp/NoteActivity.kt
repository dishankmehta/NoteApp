package com.example.noteapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.example.noteapp.viewmodel.NoteViewModel

class NoteActivity : AppCompatActivity() {

    private var mSelectedNoteId: Long? = null

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModel.NoteViewModelFactory((application as NoteApplication))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_activity)

        val titleText = findViewById<EditText>(R.id.title)
        val noteContentText = findViewById<EditText>(R.id.noteContent)
        val saveButton = findViewById<ImageButton>(R.id.saveButton)

        val intent = intent
        if (intent.hasExtra(NOTE_ID)) {
            val noteId = intent.getLongExtra(NOTE_ID, 0);
            if (noteId != 0L) {
                let {
                    noteViewModel.getNoteById(noteId).observe(this) {
                        titleText.setText(it.noteName)
                        noteContentText.setText(it.noteContent)
                        mSelectedNoteId = noteId
                    }
                }
            }
        }

        saveButton.setOnClickListener {
            performSaveAction(
                titleText = titleText.text.toString(),
                noteContent = noteContentText.text.toString()
            )
        }
    }

    private fun performSaveAction(titleText: String, noteContent: String) {
        val replyIntent = Intent()
        if (TextUtils.isEmpty(titleText) || TextUtils.isEmpty(noteContent)) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
        } else {
            replyIntent.putExtra(NOTE_TITLE, titleText)
            replyIntent.putExtra(NOTE_CONTENT, noteContent)
            replyIntent.putExtra(NOTE_ID, mSelectedNoteId)
            setResult(Activity.RESULT_OK, replyIntent)
        }
        finish()
    }

    companion object {
        const val NOTE_TITLE = "com.example.noteapp.NOTE_TITLE"
        const val NOTE_CONTENT = "com.example.noteapp.NOTE_CONTENT"
        const val NOTE_ID = "com.example.noteapp.NOTE_ID"
    }
}