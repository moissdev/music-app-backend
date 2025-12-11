package dao

import config.DatabaseFactory.dbQuery
import models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class DAOFacadeImpl : DAOFacade {

    private fun resultRowToArtista(row: ResultRow) = Artista(
        id = row[Artistas.id].value,
        name = row[Artistas.name],
        genre = row[Artistas.genre]
    )

    private fun resultRowToAlbum(row: ResultRow) = Album(
        id = row[Albumes.id].value,
        title = row[Albumes.title],
        releaseYear = row[Albumes.releaseYear],
        artistId = row[Albumes.artistId].value
    )

    private fun resultRowToTrack(row: ResultRow) = Track(
        id = row[Tracks.id].value,
        title = row[Tracks.title],
        duration = row[Tracks.duration],
        albumId = row[Tracks.albumId].value
    )

    override suspend fun allArtistas(): List<Artista> = dbQuery {
        Artistas.selectAll().map(::resultRowToArtista)
    }

    override suspend fun artista(id: UUID): Artista? = dbQuery {
        Artistas.selectAll().where { Artistas.id eq id }
            .map(::resultRowToArtista)
            .singleOrNull()
    }

    override suspend fun addNewArtista(name: String, genre: String?): Artista? = dbQuery {
        val insertStatement = Artistas.insert {
            it[Artistas.name] = name
            it[Artistas.genre] = genre
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArtista)
    }

    override suspend fun editArtista(id: UUID, name: String, genre: String?): Boolean = dbQuery {
        Artistas.update({ Artistas.id eq id }) {
            it[Artistas.name] = name
            it[Artistas.genre] = genre
        } > 0
    }

    override suspend fun deleteArtista(id: UUID): Boolean = dbQuery {
        val hasChildren = Albumes.selectAll().where { Albumes.artistId eq id }.count() > 0
        if (hasChildren) {
            return@dbQuery false // No borramos si tiene hijos
        }
        Artistas.deleteWhere { Artistas.id eq id } > 0
    }

    override suspend fun allAlbumes(): List<Album> = dbQuery {
        Albumes.selectAll().map(::resultRowToAlbum)
    }

    override suspend fun albumesByArtista(artistId: UUID): List<Album> = dbQuery {
        Albumes.selectAll().where { Albumes.artistId eq artistId }.map(::resultRowToAlbum)
    }

    override suspend fun album(id: UUID): Album? = dbQuery {
        Albumes.selectAll().where { Albumes.id eq id }
            .map(::resultRowToAlbum)
            .singleOrNull()
    }

    override suspend fun addNewAlbum(title: String, releaseYear: Int?, artistId: UUID): Album? = dbQuery {
        val insertStatement = Albumes.insert {
            it[Albumes.title] = title
            it[Albumes.releaseYear] = releaseYear
            it[Albumes.artistId] = artistId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAlbum)
    }

    override suspend fun editAlbum(id: UUID, title: String, releaseYear: Int?): Boolean = dbQuery {
        Albumes.update({ Albumes.id eq id }) {
            it[Albumes.title] = title
            it[Albumes.releaseYear] = releaseYear
        } > 0
    }

    override suspend fun deleteAlbum(id: UUID): Boolean = dbQuery {
        val hasChildren = Tracks.selectAll().where { Tracks.albumId eq id }.count() > 0
        if (hasChildren) {
            return@dbQuery false
        }
        Albumes.deleteWhere { Albumes.id eq id } > 0
    }

    override suspend fun allTracks(): List<Track> = dbQuery {
        Tracks.selectAll().map(::resultRowToTrack)
    }

    override suspend fun tracksByAlbum(albumId: UUID): List<Track> = dbQuery {
        Tracks.selectAll().where { Tracks.albumId eq albumId }.map(::resultRowToTrack)
    }

    override suspend fun track(id: UUID): Track? = dbQuery {
        Tracks.selectAll().where { Tracks.id eq id }
            .map(::resultRowToTrack)
            .singleOrNull()
    }

    override suspend fun addNewTrack(title: String, duration: Int, albumId: UUID): Track? = dbQuery {
        val insertStatement = Tracks.insert {
            it[Tracks.title] = title
            it[Tracks.duration] = duration
            it[Tracks.albumId] = albumId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTrack)
    }

    override suspend fun editTrack(id: UUID, title: String, duration: Int): Boolean = dbQuery {
        Tracks.update({ Tracks.id eq id }) {
            it[Tracks.title] = title
            it[Tracks.duration] = duration
        } > 0
    }

    override suspend fun deleteTrack(id: UUID): Boolean = dbQuery {
        // Tracks no tiene hijos, se puede borrar directo
        Tracks.deleteWhere { Tracks.id eq id } > 0
    }
}