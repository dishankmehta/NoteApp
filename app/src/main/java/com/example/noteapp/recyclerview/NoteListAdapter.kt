//package com.example.noteapp.recyclerview
//
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import com.example.noteapp.database.note.Note
//
//class NoteListAdapter : ListAdapter<Note, NoteViewHolder>(NoteComparator()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
//        return NoteViewHolder.create(parent)
//    }
//
//    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
//        val current = getItem(position)
//        holder.bind(current)
//    }
//
//    class NoteComparator : DiffUtil.ItemCallback<Note>() {
//        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
//            return oldItem === newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
//            return oldItem.nid == newItem.nid
//        }
//
//    }
//
//
//}

