package com.apm.apm.data

import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("_embedded") val embeddedArtists: EmbeddedArtists
)

data class EmbeddedArtists(
    @SerializedName("attractions") val attractions: List<Attraction>
)

data class Attraction(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String,
    @SerializedName("classifications") val classifications: List<Classification>,
    @SerializedName("images") val images: List<ImageArtists>,

)
data class Classification(
    @SerializedName("genre") val genre: Genre,

)
data class Genre(
    @SerializedName("name") val name: String,
    @SerializedName("id") val genreId: String
)
data class ImageArtists(
    @SerializedName("url") val url: String,

)



