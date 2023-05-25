package com.apm.apm.mappers

import com.apm.apm.data.BandsInTownResponse
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.objects.Concert
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConcertMapper {

    //    fun ConcertResponseToConcert(response: ConcertsResponse): Concert {
//        val venueName = response.embedded.events[0].venue.venue[0].venues
//        val date = LocalDate.parse(response.embedded.events[0].dates.start.localDate)
//        val artistName = response.embedded.events[0].name
//        val imageUrl = response.embedded.events[0].images[0].url
//        val concertImage = Picasso.get().load(imageUrl).get()
//        return Concert(venueName, date, artistName, concertImage)
//    }
    fun ConcertsResponseToConcerts(concertsResponse: ConcertsResponse): List<Concert> {
        val events = concertsResponse.embedded.events

        // Verificar si la lista de eventos está vacía
        if (events.isEmpty()) {
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
            val city = venue?.city ?: "Unknown City"
            val state = venue?.state ?: "Unknown State"
            val address = venue?.address ?: "Unknown Address"
            val location = venue?.location
            val longitude = location?.longitude
            val latitude = location?.latitude
            val price = event.priceRanges[0].min
            val currency = event.priceRanges[0].currency
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