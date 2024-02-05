package com.wenubey.crudnoteappviewui.domain

import com.wenubey.crudnoteappviewui.data.NoteDto
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String? = null,
    val title: String,
    val description: String,
    val createdAt: String,
): java.io.Serializable {
    fun mapToNoteDto(): NoteDto {
        return NoteDto(
            id = id,
            noteTitle = title,
            description = description,
            createdAt = createdAt,
        )
    }
}
