package com.apm.apm.data

import com.google.gson.annotations.SerializedName

data class MusicResponse(
    @SerializedName("_embedded")
    val embedded: EmbeddedMusic
)

data class EmbeddedMusic(
    @SerializedName("genres")
    val genres: List<MusicGenre>
)

data class MusicGenre(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
)



