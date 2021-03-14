package com.professional.im

import de.nielsfalk.ktor.swagger.ok
import de.nielsfalk.ktor.swagger.responds
import de.nielsfalk.ktor.swagger.version.v3.Example
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

/**
 * @author : bgubanov
 * @since : 14.03.2021
 **/

private const val ID = "id"

private val PipelineContext<Unit, ApplicationCall>.idParameter: Long?
    get() = call.parameters[ID]?.toLongOrNull()

private suspend fun PipelineContext<Unit, ApplicationCall>.sendBadId() =
    call.respond(HttpStatusCode.BadRequest, "Bad id")

private suspend fun PipelineContext<Unit, ApplicationCall>.withIdParameter(action: suspend (Long) -> Unit) {
    val id = idParameter
    if (id == null) sendBadId()
    else action(id)
}


fun Route.noteRoutes(noteService: NoteService) {
    route("/notes") {
        get {
            val query = call.request.queryParameters["query"]
            if (query != null)
                call.respond(noteService.getAllNotesLike(query))
            else
                call.respond(noteService.getAllNotes())
        }

        post {
            val postNote = call.receive<PostNote>()
            call.respond(HttpStatusCode.Created, noteService.createNote(postNote))
        }

        get("/{$ID}") {
            withIdParameter {
                val note = noteService.getNoteById(it)
                if (note == null) call.respond(HttpStatusCode.NotFound, "Note with this id not found")
                else call.respond(note)
            }
        }

        put("/{$ID}") {
            withIdParameter {
                val postNote = call.receive<PostNote>()
                call.respond(noteService.updateNote(it, postNote))
            }
        }
        delete("/{$ID}") {
            withIdParameter {
                call.respond(noteService.deleteNoteById(it))
            }
        }
    }
}