package com.apm.apm.mappers

import com.apm.apm.data.BandsInTownResponse
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.objects.Concert
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConcertMapper {

    fun ConcertsResponseToConcerts(concertsResponse: ConcertsResponse): List<Concert> {
        val events = concertsResponse.embedded.events

        // Verificar si la lista de eventos está vacía
        if (events.isNullOrEmpty()) {
            // Si está vacía, devolver una lista vacía
            return emptyList()
        }

        // Si hay eventos, mapearlos a objetos de tipo Concert
        val concerts = mutableListOf<Concert>()
        for (event in events) {
            val venueName = event.embeddedEvent.venue.firstOrNull()?.venues ?: "Unknown Venue"
            val date = LocalDate.parse(event.dates.start.localDate)
            val artistName = event.name
            val imageUrl = event.images[0].url
            val venue = event.embeddedEvent.venue.firstOrNull()
            val city = venue?.city?.cityName ?: "Unknown City"
            val state = venue?.state?.stateName ?: "Unknown State"
            val address = venue?.address?.addressName ?: "Unknown Address"
            val location = venue?.location
            val longitude = location?.longitude
            val latitude = location?.latitude
            val priceRange = event.priceRanges?.getOrNull(0)
            val price = priceRange?.min
            val currency = priceRange?.currency
            concerts.add(Concert(venueName, date, artistName, imageUrl, city, state, address, longitude, latitude, price, currency))
        }

        return concerts
    }

    fun BandsInTownResponseToConcert(bandsInTownResponse: BandsInTownResponse): Concert {
        val concertLocationName = bandsInTownResponse.venue.name
        val concertDate =
            LocalDateTime.parse(bandsInTownResponse.dateTime, DateTimeFormatter.ISO_DATE_TIME)
                .toLocalDate()
        val concertArtistName = bandsInTownResponse.title ?: ""
        val imageUrl = bandsInTownResponse.artist?.imageUrl

        return Concert(
            concertLocationName = concertLocationName,
            concertDate = concertDate,
            concertArtistName = concertArtistName,
            imageUrl = imageUrl
        )
    }

    fun BandsInTownListResponseToConcerts(bandsInTownList: List<BandsInTownResponse>): List<Concert> {
        return bandsInTownList.map { response ->
            BandsInTownResponseToConcert(response)
        }
    }
}