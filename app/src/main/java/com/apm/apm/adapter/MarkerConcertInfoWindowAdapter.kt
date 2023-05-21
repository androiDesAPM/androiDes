package com.apm.apm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.apm.apm.R
import com.apm.apm.objects.ConcertMapInfo
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

class MarkerInfoWindowAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(p0: Marker): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }

    override fun getInfoContents(marker: Marker): View? {
        // 1. Get tag
        val concertInfo = marker?.tag as? ConcertMapInfo ?: return null

        // 2. Inflate view and set title, address, and rating
        val view = LayoutInflater.from(context).inflate(
            R.layout.marker_concert_info, null
        )

        view.findViewById<TextView>(
            R.id.text_view_title
        ).text = concertInfo.concertArtistName

        view.findViewById<TextView>(
            R.id.text_view_address
        ).text = concertInfo.concertLocationName

        view.findViewById<TextView>(
            R.id.text_view_date
        ).text = concertInfo.concertDate.toString()

        val imagenArtista: ImageView = view.findViewById(
            R.id.image_view_artist
        )
        Picasso.get().load(concertInfo.imageUrl).into(imagenArtista)

        return view
    }

}