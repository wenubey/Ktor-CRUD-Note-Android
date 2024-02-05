package com.wenubey.crudnoteappviewui.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenubey.crudnoteappviewui.data.remote.NoteService
import com.wenubey.crudnoteappviewui.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel class responsible for managing the UI logic and data for notes.
 *
 * @param noteService The service used for interacting with remote note data.
 */
class NoteViewModel(
    private val noteService: NoteService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _title = MutableLiveData("")
    val title: LiveData<String> get() = _title

    private val _description = MutableLiveData("")
    val description: LiveData<String> get() = _description

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note> = _note

    init {
        getAllNotes()
    }

    /**
     * Handles different UI events triggered by the user interface.
     *
     * @param uiEvent The event triggered by the UI.
     */
    fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.OnGetAllNotes -> getAllNotes()
            is UiEvent.OnAddNote -> addNote()
            is UiEvent.OnDeleteNote -> deleteNote(uiEvent.id)
            is UiEvent.OnGetNote -> getNote(uiEvent.id)
            is UiEvent.OnUpdateNote -> updateNote()
        }
    }

    fun onSelectNote(note: Note) {
        _note.value = note
    }

    fun onChangeTitle(title: String) {
        _title.value = title
    }

    fun onChangeDescription(description: String) {
        _description.value = description
    }

    /**
     * Retrieves the current date and time as a formatted string.
     *
     * @return The formatted current date and time.
     */
    private fun getCurrentTime(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault())
        val currentTime = dateFormat.format(date)
        Log.w(TAG, "getCurrentTime:SUCCESS: $currentTime")
        return currentTime
    }


    private fun updateNote() = viewModelScope.launch {
        val newNote = note.value?.copy(
            title = title.value!!,
            description = description.value!!,
            createdAt = getCurrentTime()
        )
        noteService.updateNote(newNote!!.mapToNoteDto())
            .onSuccess {
                Log.w(TAG, "updateNote:SUCCESS: $it")
                _uiState.value = UiState.Success(it)
            }
            .onFailure {
                Log.e(TAG, "updateNote:ERROR: ", it)
                _uiState.value = UiState.Error(it)
            }
        getAllNotes()
    }

    private fun getNote(id: String) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        noteService.getNote(id)
            .onSuccess {
                _note.postValue(it.mapToNote())
                Log.w(TAG, "getNote:SUCCESS: $it")
            }
            .onFailure {
                Log.e(TAG, "getNote:ERROR: ", it)
                _uiState.value = UiState.Error(it)
            }

    }

    private fun deleteNote(id: String?) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        if (id != null) {
            noteService.deleteNote(id)
                .onSuccess {
                    Log.w(TAG, "deleteNote:SUCCESS: $it")
                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    Log.e(TAG, "deleteNote:ERROR: ", it)
                    _uiState.value = UiState.Error(it)
                }
        } else {
            _uiState.value = UiState.Error(Exception("Id not found!"))
            return@launch
        }

        getAllNotes()
    }

    private fun addNote() = viewModelScope.launch {
        _uiState.value = UiState.Loading
        if (title.value.isNullOrBlank() && description.value.isNullOrBlank()) {
            _uiState.value = UiState.Error(NullPointerException("Title or description is null it shouldn't be it."))
            return@launch
        }
        val newNoteDto = Note(
            title = title.value!!,
            description = description.value!!,
            createdAt = getCurrentTime()
        ).mapToNoteDto()
        noteService.addNote(newNoteDto)
            .onSuccess {
                Log.w(TAG, "addNote:SUCCESS: $it")
            }
            .onFailure {
                Log.e(TAG, "addNote:ERROR: ", it)
                _uiState.value = UiState.Error(it)
            }
        getAllNotes()
    }

    private fun getAllNotes() = viewModelScope.launch {
        _uiState.value = UiState.Loading
        val notesResult = noteService.getAllNotes()
        notesResult
            .onSuccess { notes ->
                _notes.postValue(notes.map { it.mapToNote() })
                _uiState.value = UiState.Success(null)
            }
            .onFailure {
                Log.e(TAG, "getAllNotes:ERROR: ", it)
                _uiState.value = UiState.Error(it)
            }
    }



    companion object {
        const val TAG = "NoteViewModel"
        const val SUCCESS = "Operation successfully done."
    }
}

/**
 * Sealed class representing various UI events triggered by the user interface.
 */
sealed class UiEvent {
    data object OnGetAllNotes : UiEvent()
    data class OnGetNote(val id: String) : UiEvent()
    data object OnAddNote : UiEvent()
    data object OnUpdateNote : UiEvent()
    data class OnDeleteNote(val id: String?) : UiEvent()
}

/**
 * Sealed class representing different UI states for the ViewModel.
 */
sealed class UiState {
    data object Loading : UiState()
    data class Success(val message: String? = null) : UiState()
    data class Error(val exception: Throwable) : UiState()
}