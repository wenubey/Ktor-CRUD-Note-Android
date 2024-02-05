package com.wenubey.crudnoteappviewui.data.remote

import com.wenubey.crudnoteappviewui.data.NoteDto

/**
 * A service interface for performing CRUD (Create, Read, Update, Delete) operations on notes
 * through a remote API.
 */
interface NoteService {

    suspend fun getNote(id: String): Result<NoteDto>

    suspend fun getAllNotes(): Result<List<NoteDto>>

    suspend fun deleteNote(id: String): Result<String>

    suspend fun updateNote(noteDto: NoteDto): Result<String>

    suspend fun addNote(noteDto: NoteDto): Result<String>

    companion object {
        const val BASE_URL = "http://192.168.0.20:8080"
    }

    /**
     * This class representing various endpoints (URLs) for interacting with notes on the remote server.
     */
    sealed class Endpoints(val url: String) {

        data object GetAllNotes: Endpoints("$BASE_URL/notes")

        data object AddNote: Endpoints("$BASE_URL/notes")

        data class GetNoteById(val noteId: String): Endpoints("$BASE_URL/notes/${noteId}")

        data class UpdateNote(val noteId: String): Endpoints("$BASE_URL/notes/${noteId}")

        data class DeleteNote(val noteId: String): Endpoints("$BASE_URL/notes/${noteId}")


    }
}