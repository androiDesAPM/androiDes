package com.apm.apm.mappers

import com.apm.apm.data.MusicResponse
import com.apm.apm.objects.Genre

class GenreMapper {

    fun MusicResponseToGenre(musicResponse: MusicResponse): List<Genre> {
        val musicGenres = musicResponse.embedded.genres
        if (musicGenres.isEmpty()) {
            return emptyList()
        }
        val genres = mutableListOf<Genre>()
        for (genre in musicGenres) {
            val id = genre.id
            val name = genre.name
            genres.add(Genre(id, name))
        }
        return genres
    }
}