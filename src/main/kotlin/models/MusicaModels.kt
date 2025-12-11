package models

import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}

// --- ARTISTAS ---
@Serializable
data class Artista(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val genre: String?
)

@Serializable
data class NuevoArtista(
    val name: String,
    val genre: String?
)

// --- ALBUMES ---
@Serializable
data class Album(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val title: String,
    val releaseYear: Int?,
    @Serializable(with = UUIDSerializer::class) val artistId: UUID
)

@Serializable
data class NuevoAlbum(
    val title: String,
    val releaseYear: Int?,
    @Serializable(with = UUIDSerializer::class) val artistId: UUID
)

// --- TRACKS ---
@Serializable
data class Track(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val title: String,
    val duration: Int,
    @Serializable(with = UUIDSerializer::class) val albumId: UUID
)

@Serializable
data class NuevoTrack(
    val title: String,
    val duration: Int,
    @Serializable(with = UUIDSerializer::class) val albumId: UUID
)