package com.wenubey.crudnoteappviewui.ui.note_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wenubey.crudnoteappviewui.databinding.FragmentNotesBinding
import com.wenubey.crudnoteappviewui.domain.Note
import com.wenubey.crudnoteappviewui.ui.NoteViewModel
import com.wenubey.crudnoteappviewui.ui.UiEvent
import com.wenubey.crudnoteappviewui.ui.UiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class NotesFragment : Fragment() {

    private lateinit var _binding: FragmentNotesBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var addNoteFab: FloatingActionButton

    private val viewModel: NoteViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = _binding.root
        bindViews()
        setupRecyclerView()
        observeUiState()
        onFABClicked()
        return view
    }

    private fun onFABClicked() {
        addNoteFab.setOnClickListener {
            navigateToDetailScreen(null)
        }
    }

    private fun bindViews() {
        progressBar = _binding.progressBar
        recyclerView = _binding.recyclerView
        noteAdapter = NoteAdapter { note -> navigateToDetailScreen(note) }
        addNoteFab = _binding.addNoteFab

    }



    private fun showMessage(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        val swipeToDeleteCallback = SwipeToDeleteCallback(requireContext()) { position ->
            val deletedItem = noteAdapter.currentList[position]
           viewModel.onUiEvent(UiEvent.OnDeleteNote(deletedItem.id))
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        viewModel.notes.observe(viewLifecycleOwner) {
            noteAdapter.submitList(it as MutableList<Note>)
        }

    }


    private fun navigateToDetailScreen(note: Note?) {
        val action = NotesFragmentDirections
            .actionNotesListFragmentToAddEditNoteFragment().apply {
                noteArg = note
            }
        findNavController().navigate(action)

    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state) {
                    is UiState.Loading -> {
                        showLoading(true)
                    }
                    is UiState.Error -> {
                        showMessage(state.exception.message)
                    }
                    is UiState.Success -> {
                        showLoading(false)
                        state.message?.let {
                            showMessage(it)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUiEvent(UiEvent.OnGetAllNotes)
    }

    companion object {
        const val TAG = "NotesFragment"
    }
}