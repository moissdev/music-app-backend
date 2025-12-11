package dao

import models.*
import java.util.UUID

interface DAOFacade {
    // --- ARTISTAS ---
    suspend fun allArtistas(): List<Artista>
    suspend fun artista(id: UUID): Artista?
    suspend fun addNewArtista(name: String, genre: String?): Artista?
    suspend fun editArtista(id: UUID, name: String, genre: String?): Boolean
    suspend fun deleteArtista(id: UUID): Boolean

    // --- ALBUMES ---
    suspend fun allAlbumes(): List<Album>
    suspend fun albumesByArtista(artistId: UUID): List<Album>
    suspend fun album(id: UUID): Album?
    suspend fun addNewAlbum(title: String, releaseYear: Int?, artistId: UUID): Album?
    suspend fun editAlbum(id: UUID, title: String, releaseYear: Int?): Boolean
    suspend fun deleteAlbum(id: UUID): Boolean

    // --- TRACKS ---
    suspend fun allTracks(): List<Track>
    suspend fun tracksByAlbum(albumId: UUID): List<Track>
    suspend fun track(id: UUID): Track?
    suspend fun addNewTrack(title: String, duration: Int, albumId: UUID): Track?
    suspend fun editTrack(id: UUID, title: String, duration: Int): Boolean
    suspend fun deleteTrack(id: UUID): Boolean
}