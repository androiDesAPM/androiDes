package com.apm.apm.objects

import java.time.LocalDate

data class ConcertMapInfo(
    val concertLocationName: String,
    val concertDate: LocalDate,
    var concertArtistName: String,
    val imageUrl: String? = null
)