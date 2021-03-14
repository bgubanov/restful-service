package com.professional.im

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


/**
 * @author : bgubanov
 * @since : 14.03.2021
 **/

@Serializable
data class Note(
    val id: Long,
    val title: String?,
    val content: String
)

@Serializable
data class PostNote(
    val title: String? = null,
    val content: String
)

object Notes: Table() {
    val id = long("id").autoIncrement()
    val title = varchar("title", 255).nullable()
    val content = varchar("content", 512)
    override val primaryKey = PrimaryKey(id)
}