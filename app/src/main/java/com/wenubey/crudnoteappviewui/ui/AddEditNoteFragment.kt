package com.wenubey.crudnoteappviewui.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.wenubey.crudnoteappviewui.R
import com.wenubey.crudnoteappviewui.databinding.FragmentAddEditNoteBinding
import com.wenubey.crudnoteappviewui.databinding.FragmentNotesBinding
import com.wenubey.crudnoteappviewui.domain.Note
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class AddEditNoteFragment : Fragment() {

    private lateinit var _binding: FragmentAddEditNoteBinding
    private lateinit var titleTextField: TextInputLayout
    private lateinit var descriptionTextField: TextInputLayout
    private lateinit var fabSave: FloatingActionButton
    private lateinit var progressBar: ProgressBar

    private val args: AddEditNoteFragmentArgs by navArgs()

    private val viewModel: NoteViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        val view = _binding.root
        bindViews()
        observeUiState()
        addValidationListeners()
        val note = args.noteArg

        note?.let {
            viewModel.onSelectNote(note)
            titleTextField.editText?.setText(note.title)
            descriptionTextField.editText?.setText(note.description)
        }

        val fabSrc = if (note != null) R.drawable.ic_save else R.drawable.ic_add

        fabSave.apply {
            setImageResource(fabSrc)
        }

        fabOnClick(note)
        return view
    }

    private fun addValidationListeners() {
        val textInputLayouts = listOf(titleTextField, descriptionTextField)

        for (textInputLayout in textInputLayouts) {
            val editText = textInputLayout.editText

            editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val isTextEmpty = s?.trim().isNullOrEmpty()
                    textInputLayout.error = if (isTextEmpty) "It shouldn't be empty!" else null
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            val isTextEmpty = editText?.text.toString().trim().isEmpty()
            if (isTextEmpty) {
                textInputLayout.error = "It shouldn't be empty!"
            }
        }
    }


    private fun bindViews() {
        titleTextField = _binding.titleTextField
        descriptionTextField = _binding.descriptionTextField
        fabSave = _binding.fabSave
        progressBar = _binding.progressBar
    }

    private fun fabOnClick(
        note: Note?
    ) {
        fabSave.setOnClickListener {
            viewModel.onChangeTitle(titleTextField.editText?.text.toString())
            viewModel.onChangeDescription(descriptionTextField.editText?.text.toString())
            // is update or add
            if (note != null) {
                viewModel.onUiEvent(UiEvent.OnUpdateNote)
            } else {
                viewModel.onUiEvent(UiEvent.OnAddNote)
            }
            navigateToListScreen()
        }
    }

    private fun navigateToListScreen() {
        val action = AddEditNoteFragmentDirections.actionAddEditNoteFragmentToNotesListFragment()
        findNavController().navigate(action)
    }

    private fun showMessage(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
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

    companion object {
        const val TAG = "AddEditNoteFragment"
    }
}
