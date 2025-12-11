package routes

import dao.DAOFacade
import dao.DAOFacadeImpl
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.*
import java.util.UUID

fun Route.musicaRoutes() {
    val dao: DAOFacade = DAOFacadeImpl()

    // --- RUTAS DE ARTISTAS ---
    route("/artistas") {
        get {
            call.respond(dao.allArtistas())
        }

        get("/{id}") {
            val id = runCatching { UUID.fromString(call.parameters["id"]) }.getOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }
            val artista = dao.artista(id)
            if (artista == null) {
                call.respond(HttpStatusCode.NotFound, "Artista no encontrado")
            } else {
                call.respond(artista)
            }
        }

        post {
            val form = call.receive<NuevoArtista>()
            val nuevo = dao.addNewArtista(form.name, form.genre)
            if (nuevo != null) call.respond(HttpStatusCode.Created, nuevo)
            else call.respond(HttpStatusCode.InternalServerError)
        }

        put("/{id}") {
            val id = runCatching { UUID.fromString(call.parameters["id"]) }.getOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val form = call.receive<NuevoArtista>()
            val actualizado = dao.editArtista(id, form.name, form.genre)
            if (actualizado) call.respond(HttpStatusCode.OK, "Artista actualizado")
            else call.respond(HttpStatusCode.NotFound)
        }

        delete("/{id}") {
            val id = runCatching { UUID.fromString(call.parameters["id"]) }.getOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val eliminado = dao.deleteArtista(id)
            if (eliminado) {
                call.respond(HttpStatusCode.OK, "Artista eliminado")
            } else {
                // Aquí cae si tiene álbumes asociados (Protección Cascada)
                call.respond(HttpStatusCode.Conflict, "No se puede eliminar: El artista tiene álbumes asociados.")
            }
        }
    }

    // --- RUTAS DE ALBUMES ---
    route("/albumes") {
        get {
            call.respond(dao.allAlbumes())
        }

        get("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val album = dao.album(id)
            if (album == null) call.respond(HttpStatusCode.NotFound) else call.respond(album)
        }

        post {
            val form = call.receive<NuevoAlbum>()
            val nuevo = dao.addNewAlbum(form.title, form.releaseYear, form.artistId)
            if (nuevo != null) call.respond(HttpStatusCode.Created, nuevo)
            else call.respond(HttpStatusCode.InternalServerError)
        }

        delete("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val eliminado = dao.deleteAlbum(id)
            if (eliminado) {
                call.respond(HttpStatusCode.OK, "Album eliminado")
            } else {
                call.respond(HttpStatusCode.Conflict, "No se puede eliminar: El álbum tiene tracks asociados.")
            }
        }
    }

    // --- RUTAS DE TRACKS ---
    route("/tracks") {
        get {
            call.respond(dao.allTracks())
        }

        post {
            val form = call.receive<NuevoTrack>()
            val nuevo = dao.addNewTrack(form.title, form.duration, form.albumId)
            if (nuevo != null) call.respond(HttpStatusCode.Created, nuevo)
            else call.respond(HttpStatusCode.InternalServerError)
        }

        delete("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val eliminado = dao.deleteTrack(id)
            if (eliminado) call.respond(HttpStatusCode.OK, "Track eliminado")
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}