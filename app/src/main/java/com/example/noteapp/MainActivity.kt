package com.example.noteapp

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.database.entity.Note
import com.example.noteapp.recyclerview.NoteListRecyclerAdapter
import com.example.noteapp.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteListRecyclerAdapter.OnItemClickListener {

    private val newNoteActivityRequestCode = 1
    private lateinit var adapter: NoteListRecyclerAdapter
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModel.NoteViewModelFactory((application as NoteApplication))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.noteRecycleView)
        adapter = NoteListRecyclerAdapter()
        adapter.setItemClickListener(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        noteViewModel.allNotes.observe(owner = this) { notes ->
            notes.let {
                adapter.setNotes(it)
            }
        }

        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val title = result.data?.getStringExtra(NoteActivity.NOTE_TITLE)
                val content = result.data?.getStringExtra(NoteActivity.NOTE_CONTENT)
                val noteId: Long? = result.data?.getLongExtra(NoteActivity.NOTE_ID, 0)
                let {
                    val note = Note(title, content)
                    if (noteId != 0L) {
                        note.nid = noteId
                    }
                    noteViewModel.upsert(note)
                    val toast: Toast = Toast.makeText(this, "Title: $title Content: $content", Toast.LENGTH_SHORT)
                    toast.show()
                }
            } else {
                Toast.makeText(applicationContext, "Not Saved!!!", Toast.LENGTH_LONG).show()
            }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
//            activityResultLauncher.launch(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(NoteActivity.NOTE_TITLE)
            val content = data?.getStringExtra(NoteActivity.NOTE_CONTENT)
            val noteId: Long? = data?.getLongExtra(NoteActivity.NOTE_ID, 0)
            let {
                val note = Note(title, content)
                if (noteId != 0L) {
                    note.nid = noteId
                }
                noteViewModel.upsert(note)
                val toast: Toast = Toast.makeText(this, "Title: $title Content: $content", Toast.LENGTH_SHORT)
                toast.show()
            }
        } else {
            Toast.makeText(applicationContext, "Not Saved!!!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemClick(itemView: View, position: Int) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra(NoteActivity.NOTE_ID, adapter.getNote(position).nid)
        startActivityForResult(intent, newNoteActivityRequestCode)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_change_theme) {
            val isNightTheme: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (isNightTheme == Configuration.UI_MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}