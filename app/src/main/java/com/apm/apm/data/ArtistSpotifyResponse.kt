package com.apm.apm.data

import com.google.gson.annotations.SerializedName

data class ArtistSpotifyResponse(
    @SerializedName("artists")
    val artists: Artists
)

data class Artists(
    @SerializedName("items")
    val items: List<Items>
)

data class Items(
    @SerializedName("uri")
    val artistUri: String,
    @SerializedName("name")
    val artistName: String,
    @SerializedName("id")
    val spotifyId: String,
    @SerializedName("images")
    val images: List<ImageSpotify>,
    @SerializedName("genres")
    val genres: List<String>
)

data class ImageSpotify(
    @SerializedName("url")
    val url: String
)
