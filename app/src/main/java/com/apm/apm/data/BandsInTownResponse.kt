package com.apm.apm.data

import com.google.gson.annotations.SerializedName

data class BandsInTownResponse (

    @SerializedName("datetime") val dateTime: String,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: Artist,
    @SerializedName("venue") val venue: VenueBandsInTown,
    )

    data class Artist(
        @SerializedName("name") val name: String,
        @SerializedName("image_url") val imageUrl: String
    )

    data class VenueBandsInTown(
        @SerializedName("name") val name: String
)