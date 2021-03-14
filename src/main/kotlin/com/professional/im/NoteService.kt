package com.professional.im

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * @author : bgubanov
 * @since : 14.03.2021
 **/

class NoteService(val n: Int) {

    suspend fun getAllNotes() = newSuspendedTransaction {
        Notes.selectAll().map { it.toNote() }
    }

    suspend fun getAllNotesLike(query: String) = newSuspendedTransaction {
        Notes.selectAll().map { it.toNote() }.filter {
            it.content.contains(query, true) ||
                    (it.title?.contains(query, true) ?: false)
        }
    }

    suspend fun getNoteById(id: Long) = newSuspendedTransaction {
        Notes.select { Notes.id eq id }.singleOrNull()?.toNote()
    }

    suspend fun deleteNoteById(id: Long) = newSuspendedTransaction {
        Notes.deleteWhere { Notes.id eq id }
    }

    suspend fun createNote(postNote: PostNote) = newSuspendedTransaction {
        val insertion = Notes.insert {
            it[title] = postNote.title
            it[content] = postNote.content
        }
        Note(insertion[Notes.id], insertion[Notes.title], insertion[Notes.content])
     }

    suspend fun updateNote(id: Long, postNote: PostNote) = newSuspendedTransaction {
        Notes.update({Notes.id eq id}) {
            it[title] = postNote.title
            it[content] = postNote.content
        }
    }

    private fun ResultRow.toNote(): Note {
        val id = get(Notes.id)
        val content = get(Notes.content)
        val title = get(Notes.title) ?: content.take(n)
        return Note(id, title, content)
    }
}