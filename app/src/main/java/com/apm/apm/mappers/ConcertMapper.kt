package com.apm.apm.mappers

import com.apm.apm.data.BandsInTownResponse
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.objects.Concert

class ConcertMapper {

    fun ConcertsResponseToConcerts(concertsResponse: ConcertsResponse): List<Concert> {

        // Verificar si la lista de eventos está vacía
        if (concertsResponse.embedded == null || concertsResponse.embedded.events.isNullOrEmpty()) {
            // Si está vacía, devolver una lista vacía
            return emptyList()
        }

        val events = concertsResponse.embedded.events

        // Si hay eventos, mapearlos a objetos de tipo Concert
        val concerts = mutableListOf<Concert>()
        for (event in events) {
            val idEvent = event.id
            val venueName = event.embeddedEvent.venue?.firstOrNull()?.venues ?: "Unknown Venue"
            val date = event.dates.start.localDate
            val artistName = event.name
            val imageUrl = event.images[0].url
            val venue = event.embeddedEvent.venue?.firstOrNull()
            val city = venue?.city?.cityName ?: "Unknown City"
            val state = venue?.state?.stateName ?: "Unknown State"
            val address = venue?.address?.addressName ?: "Unknown Address"
            val location = venue?.location
            val longitude = location?.longitude
            val latitude = location?.latitude
            val priceRange = event.priceRanges?.getOrNull(0)
            val price = priceRange?.min
            val currency = priceRange?.currency
            concerts.add(Concert(idEvent, venueName, date, artistName, imageUrl, city, state, address, longitude, latitude, price, currency))
        }

        return concerts
    }

    fun BandsInTownResponseToConcert(bandsInTownResponse: BandsInTownResponse): Concert {
        val concertLocationName = bandsInTownResponse.venue.name
        val concertDate = "0001-01-01"
        val concertArtistName = bandsInTownResponse.title ?: ""
        val imageUrl = bandsInTownResponse.artist?.imageUrl
        val eventId = "0"

        return Concert(
            ticketMasterEventId = eventId,
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