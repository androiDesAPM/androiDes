package com.apm.apm.mappers

import com.apm.apm.data.ArtistTicketMasterResponse

class ArtistTicketMasterMapper {

    //Busca el artista que coincide exactamente con el nombre buscado
    fun artistResponseToArtistOptimized(artistResponse: ArtistTicketMasterResponse, artistName: String): String {
        return if (artistResponse.embeddedArtists.attractions.isNotEmpty()) {
            for (attraction in artistResponse.embeddedArtists.attractions) {
                if ((attraction.name.lowercase()) == artistName.lowercase()){
                    return attraction.id
                }
            }
            val attraction = artistResponse.embeddedArtists.attractions.firstOrNull()
                ?: throw IllegalArgumentException("The attraction list is empty")
            attraction.id
        } else {
            artistResponseToArtist(artistResponse)
        }
    }

    //Devuelve el primer artista de la lista
    private fun artistResponseToArtist(artistResponse: ArtistTicketMasterResponse): String {
        val attraction = artistResponse.embeddedArtists.attractions.firstOrNull()
            ?: throw IllegalArgumentException("The attraction list is empty")
        return attraction.id
    }
}