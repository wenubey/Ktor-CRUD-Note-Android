package com.wenubey.crudnoteappviewui.ui.note_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wenubey.crudnoteappviewui.databinding.NoteItemBinding
import com.wenubey.crudnoteappviewui.domain.Note

class NoteAdapter(
    private val onClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback) {

    class NoteViewHolder(binding: NoteItemBinding, val onClick: (Note) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private val titleTV = binding.titleTV
        private val descriptionTV = binding.descriptionTV
        private var currentNote: Note? = null

        init {
            binding.noteCard.setOnClickListener {
                currentNote?.let {
                    onClick(it)
                }
            }
        }

        fun bind(note: Note) {
            currentNote = note

            titleTV.text = note.title
            descriptionTV.text = note.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NoteItemBinding.inflate(inflater, parent, false)
        return NoteViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }
}

object NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }
}
