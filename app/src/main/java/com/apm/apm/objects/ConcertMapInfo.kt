package com.apm.apm.objects

import java.time.LocalDate

class ConcertMapInfo(
val concertLocationName: String,
val concertDate: LocalDate,
val concertArtistName: String,
val imageUrl: String? = null
)