package com.wenubey.crudnoteappviewui.data.remote

import android.util.Log
import com.wenubey.crudnoteappviewui.data.NoteDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NoteServiceImpl(
    private val client: HttpClient,
    private val ioDispatcher: CoroutineDispatcher,
) : NoteService {


    /**
     * Retrieves a specific note by its unique identifier.
     *
     * @param id The unique identifier of the note to retrieve.
     * @return [Result] representing the outcome of the operation. If successful, it contains
     * the retrieved [NoteDto]; otherwise, it contains an exception.
     */
    override suspend fun getNote(id: String): Result<NoteDto> {
        return withContext(ioDispatcher) {
            val response: HttpResponse = client.get(NoteService.Endpoints.GetNoteById(id).url)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val result = response.body<NoteDto>()
                    Log.w(TAG, "getNote:Success")
                    Result.success(result)
                }

                else -> {
                    Log.e(TAG, "getNote:Error: HTTP status code: ${response.status}")
                    Result.failure(httpException(response.status))
                }
            }
        }
    }

    /**
     * Retrieves a list of all notes.
     *
     * @return [Result] representing the outcome of the operation. If successful, it contains
     * a list of [NoteDto] representing all notes; otherwise, it contains an exception.
     */
    override suspend fun getAllNotes(): Result<List<NoteDto>> {
        return withContext(ioDispatcher) {
            val response: HttpResponse = client.get(NoteService.Endpoints.GetAllNotes.url)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val result = response.body<List<NoteDto>>()
                    Log.w(TAG, "getAllNotes:Success")
                    Result.success(result)
                }

                else -> {
                    Log.e(TAG, "getAllNotes:Error: HTTP status code: ${response.status}")
                    Result.failure(httpException(response.status))
                }
            }
        }
    }

    /**
     * Deletes a note by its unique identifier.
     *
     * @param id The unique identifier of the note to delete.
     * @return [Result] representing the outcome of the operation. If successful, it contains
     * a success message; otherwise, it contains an exception.
     */
    override suspend fun deleteNote(id: String): Result<String> {
        return withContext(ioDispatcher) {
            val response: HttpResponse = client.delete(NoteService.Endpoints.DeleteNote(id).url)
            when (response.status) {
                HttpStatusCode.OK -> {
                    Log.w(TAG, "getAllNotes:Success")
                    Result.success("Delete $SUCCESS")
                }

                else -> {
                    Log.e(TAG, "deleteNote:Error: HTTP status code: ${response.status}")
                    Result.failure(httpException(response.status))
                }
            }
        }
    }

    /**
     * Updates an existing note with the provided data.
     *
     * @param noteDto The [NoteDto] containing the updated note data.
     * @return [Result] representing the outcome of the operation. If successful, it contains
     * a success message; otherwise, it contains an exception.
     */
    override suspend fun updateNote(noteDto: NoteDto): Result<String> {
        return withContext(ioDispatcher) {
            val response: HttpResponse =
                client.put(NoteService.Endpoints.UpdateNote(noteDto.id ?: "").url) {
                    contentType(ContentType.Application.Json)
                    setBody(noteDto)
                }
            when (response.status) {
                HttpStatusCode.OK -> {
                    Log.w(TAG, "updateNote:Success")
                    Result.success("Update $SUCCESS")
                }

                else -> {
                    Log.e(TAG, "updateNote:Error: HTTP status code: ${response.status}")
                    Result.failure(httpException(response.status))
                }
            }
        }

    }

    /**
     * Adds a new note with the provided data.
     *
     * @param noteDto The [NoteDto] containing the data of the new note to be added.
     * @return [Result] representing the outcome of the operation. If successful, it contains
     * a success message; otherwise, it contains an exception.
     */
    override suspend fun addNote(noteDto: NoteDto): Result<String> {
        return withContext(ioDispatcher) {
            val response: HttpResponse = client.post(NoteService.Endpoints.AddNote.url) {
                contentType(ContentType.Application.Json)
                setBody(noteDto)
            }
            when(response.status) {
                HttpStatusCode.Created -> {
                    Log.w(TAG, "addNote:Success")
                    Result.success("Add note $SUCCESS")
                }
                else -> {
                    Log.e(TAG, "addNote:Error: HTTP status code: ${response.status}")
                    Result.failure(httpException(response.status))
                }
            }
        }

    }

    private fun httpException(statusCode: HttpStatusCode): RuntimeException =
        RuntimeException("There is an error occurred: HTTP status code: $statusCode")

    companion object {
        const val TAG = "NoteServiceImpl"
        const val SUCCESS = "operation successful."

    }
}