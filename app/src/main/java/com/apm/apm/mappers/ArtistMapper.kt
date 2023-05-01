package com.apm.apm.mappers

import com.apm.apm.data.ArtistResponse
import com.apm.apm.objects.Artist

class ArtistMapper {

    fun ArtistResponseToArtist(artistResponse: ArtistResponse): Artist {
        val attraction = artistResponse.embeddedArtists.attractions.firstOrNull()
            ?: throw IllegalArgumentException("The attraction list is empty")
        val genreName = attraction.classifications.firstOrNull()?.genre?.name ?: ""
        val images = attraction.images
        val imageUrl = images[0].url
        return Artist(attraction.id, attraction.name, imageUrl, genreName)
    }
}