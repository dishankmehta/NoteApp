package com.example.noteapp.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.database.entity.Note

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private lateinit var noteTitleItemView: TextView
    private lateinit var noteContentItemView: TextView

    constructor(itemView: View, onItemClickListener: NoteListRecyclerAdapter.OnItemClickListener): this(itemView) {
        noteTitleItemView = itemView.findViewById(R.id.recyclerviewItemTitleId)
        noteContentItemView = itemView.findViewById(R.id.recyclerviewItemContentId)

        itemView.setOnClickListener {
            val pos: Int = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(itemView, pos)
            }
        }
    }

    fun bind(note: Note) {
        noteTitleItemView.text = note.noteName
        noteContentItemView.text = note.noteContent
    }

    companion object {
        fun create(parent: ViewGroup, onItemClickListener: NoteListRecyclerAdapter.OnItemClickListener): NoteViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
            return NoteViewHolder(view, onItemClickListener)
        }
    }
}