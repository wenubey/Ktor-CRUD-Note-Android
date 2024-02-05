package com.wenubey.crudnoteappviewui.data

import com.wenubey.crudnoteappviewui.domain.Note
import kotlinx.serialization.Serializable


@Serializable
data class NoteDto(
    val id: String? = null,
    val noteTitle: String,
    val description: String,
    val createdAt: String,
): java.io.Serializable {
    fun mapToNote(): Note {
        return Note(
            id = id,
            title = noteTitle,
            description = description,
            createdAt = createdAt
        )
    }
}
