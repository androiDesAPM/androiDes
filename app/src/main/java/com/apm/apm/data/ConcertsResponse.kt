package com.apm.apm.data

data class ConcertsResponse(
    val datetime: String,
    val artist: Artist,
    val venue: Venue
) {
    data class Artist(
        val name: String
    )

    data class Venue(
        val name: String
    )
}
