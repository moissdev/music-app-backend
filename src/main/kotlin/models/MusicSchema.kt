package models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime // Asegúrate de importar esto
import java.time.LocalDateTime

// 1. Tabla ARTISTAS
object Artistas : UUIDTable("artistas") {
    val name = varchar("name", 100)
    val genre = varchar("genre", 50).nullable()
    // Exposed maneja las fechas con java.time.LocalDateTime
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}

// 2. Tabla ALBUMES
object Albumes : UUIDTable("albumes") {
    val title = varchar("title", 150)
    val releaseYear = integer("release_year").nullable()

    val artistId = reference("artist_id", Artistas, onDelete = ReferenceOption.RESTRICT)

    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}

// 3. Tabla TRACKS
object Tracks : UUIDTable("tracks") {
    val title = varchar("title", 150)
    val duration = integer("duration") // Duración en segundos

    val albumId = reference("album_id", Albumes, onDelete = ReferenceOption.RESTRICT)

    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}