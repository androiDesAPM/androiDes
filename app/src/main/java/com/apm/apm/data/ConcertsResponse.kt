package com.apm.apm.data

import com.google.gson.annotations.SerializedName
data class ConcertsResponse(
    @SerializedName("_embedded")
    val embedded: Embedded
)
data class Embedded(
    @SerializedName("events")
    val events: List<Event>
)

data class Event(
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("dates")
    val dates: Dates,
    @SerializedName("_embedded")
    val embeddedEvent: EmbeddedEvent
)
data class Dates(
    @SerializedName("start")
    val start: Start
)
data class Start(
    @SerializedName("localDate")
    val localDate: String
)
data class EmbeddedEvent(
    @SerializedName("venues")
    val venue: List<Venue>,
    @SerializedName("attractions")
    val attractions: List<Attraction>
)

data class Venue(
    @SerializedName("name")
    val venues: String,
    @SerializedName("location")
    val location: Location
)

data class Location(
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("latitude")
    val latitude: String
)

data class Image(
    @SerializedName("url")
    val url: String
)
