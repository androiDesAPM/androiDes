package com.apm.apm.mappers

import com.apm.apm.data.Event
import com.apm.apm.objects.ConcertMapInfo
import java.time.LocalDate

class EventMapper {

    fun EventToConcertMapInfo(event: Event): ConcertMapInfo {

        //Valores por defecto en caso de que falte información en la llamda a la Api
        var artistName = "Artista Desconocido"
        var date = LocalDate.of(1,1,1)
        var locationName = "Ubicación Desconocida"
        var imageUrl = ""

        try {
            artistName = event.embeddedEvent.attractions[0].name
        }catch (e:Exception){
        }
        try{
            date = LocalDate.parse(event.dates.start.localDate)
        }catch (e:Exception){
        }
        try{
            locationName = event.embeddedEvent.venue[0].venues
        }catch (e:Exception){
        }
        try{
            imageUrl = event.embeddedEvent.attractions[0].images[0].url
        }catch (e:Exception){
        }
        return ConcertMapInfo(locationName, date, artistName, imageUrl)
    }
}