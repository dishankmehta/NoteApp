package com.example.noteapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.database.entity.Note
import com.example.noteapp.recyclerview.NoteListRecyclerAdapter
import com.example.noteapp.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment(), NoteListRecyclerAdapter.OnItemClickListener {

    private lateinit var adapter: NoteListRecyclerAdapter
    private var navController: NavController? = null
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModel.NoteViewModelFactory((requireActivity().application as NoteApplication))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(newNoteActivityRequestCode) { requestKey, bundle ->
            if (requestKey == newNoteActivityRequestCode) {
                val title = bundle.getString(NoteActivity.NOTE_TITLE)
                val content = bundle.getString(NoteActivity.NOTE_CONTENT)
                val noteId: Long? = bundle.getLong(NoteActivity.NOTE_ID, 0)
                let {
                    val note = Note(title, content)
                    if (noteId != 0L) {
                        note.nid = noteId
                    }
                    noteViewModel.upsert(note)
                    val toast: Toast = Toast.makeText(activity, "Title: $title Content: $content", Toast.LENGTH_SHORT)
                    toast.show()
                }
            } else {
                Toast.makeText(context?.applicationContext, "Not Saved!!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.noteRecycleView)
        adapter = NoteListRecyclerAdapter()
        adapter.setItemClickListener(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


        noteViewModel.allNotes.observe(owner = requireActivity()) { notes ->
            notes.let {
                adapter.setNotes(it)
            }
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            navController?.navigate(R.id.action_list_to_note)
        }
    }

    override fun onItemClick(itemView: View, position: Int) {
        val bundle = bundleOf(
            NoteFragment.SELECTED_NOTE to adapter.getNote(position).nid
        )
        navController?.navigate(R.id.action_list_to_note, bundle)
    }

    companion object {
        const val newNoteActivityRequestCode = "1"
    }
}