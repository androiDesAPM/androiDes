package com.apm.apm.data

import com.google.gson.annotations.SerializedName

data class ConcertsResponse(

    @SerializedName("starts_at")
    val starts_at: String,

    @SerializedName("artist")
    val artist: Artist,

    @SerializedName("venue")
    val venue: Venue


) {
    data class Artist(
        @SerializedName("name")
        val nameArtist: String
    )

    data class Venue(
        @SerializedName("name")
        val nameVenue: String
    )
}
