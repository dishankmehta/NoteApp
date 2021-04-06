package com.example.noteapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.noteapp.viewmodel.NoteViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [NoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteFragment : Fragment() {

    private var mSelectedNoteId: Long? = null
    private var navController: NavController? = null
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModel.NoteViewModelFactory((requireActivity().application as NoteApplication))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mSelectedNoteId = it.getLong(SELECTED_NOTE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val titleText = view.findViewById<EditText>(R.id.title)
        val noteContentText = view.findViewById<EditText>(R.id.noteContent)
        val saveButton = view.findViewById<ImageButton>(R.id.saveButton)

        if (mSelectedNoteId != null) {
            let {
                mSelectedNoteId?.let { noteId ->
                    noteViewModel.getNoteById(noteId).observe(requireActivity()) {
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

    private fun performSaveAction(titleText: String,
                                  noteContent: String) {
        val bundle = bundleOf(
            NOTE_TITLE to titleText,
            NOTE_CONTENT to noteContent,
            NOTE_ID to mSelectedNoteId
        )
        setFragmentResult(ListFragment.newNoteActivityRequestCode, bundle)
        navController?.navigate(R.id.action_note_to_list)
    }

    companion object {
        const val SELECTED_NOTE = "selectedNote"
        const val NOTE_TITLE = "com.example.noteapp.NOTE_TITLE"
        const val NOTE_CONTENT = "com.example.noteapp.NOTE_CONTENT"
        const val NOTE_ID = "com.example.noteapp.NOTE_ID"
    }
}