package com.apm.apm.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
data class ConcertsResponse(
    @SerializedName("_embedded")
    val embedded: Embedded
)
data class Embedded(
    @SerializedName("events")
    val events: List<Events>
)

data class Events(
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("dates")
    val dates: Dates,
    @SerializedName("_embedded")
    val venue: Venue
)
data class Dates(
    @SerializedName("start")
    val start: Start
)
data class Start(
    @SerializedName("localDate")
    val localDate: String
)
data class Venue(
    @SerializedName("venues")
    val venue: List<Venues>
)

data class Venues(
    @SerializedName("name")
    val venues: String
)

data class Image(
    @SerializedName("url")
    val url: String
)
