package com.apm.apm.mappers

import com.apm.apm.data.ConcertsResponse
import com.apm.apm.objects.Concert
import com.squareup.picasso.Picasso
import java.time.LocalDate

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
            val venueName = event.venue.venue.firstOrNull()?.venues ?: "Unknown Venue"
            val date = LocalDate.parse(event.dates.start.localDate)
            val artistName = event.name
            val imageUrl = event.images[0].url
            concerts.add(Concert(venueName, date, artistName, imageUrl))
        }

        return concerts
    }
}