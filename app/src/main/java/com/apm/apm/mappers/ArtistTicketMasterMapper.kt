package com.apm.apm.mappers

import com.apm.apm.data.ArtistTicketMasterResponse

class ArtistTicketMasterMapper {

    fun artistResponseToArtist(artistResponse: ArtistTicketMasterResponse): String {
        val attraction = artistResponse.embeddedArtists.attractions.firstOrNull()
            ?: throw IllegalArgumentException("The attraction list is empty")
        return attraction.id
    }
}