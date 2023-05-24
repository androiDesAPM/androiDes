package com.apm.apm.mappers

import com.apm.apm.data.ArtistSpotifyResponse
import com.apm.apm.objects.Artist

class ArtistSpotifyMapper {

    fun artistResponseToArtist(artistResponse: ArtistSpotifyResponse): Artist {
        val artist = artistResponse.artists.items[0]
        val id = artist.artistId
        val name = artist.artistName
        val genres = artist.genres
        val images = artist.images
        val imageUrl = images[0].url
        val uriSpotify = artist.artistUri
        return Artist(id, name, imageUrl, genres, uriSpotify)
    }
}