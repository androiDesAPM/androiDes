package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment

class ConcertsFromFavArstistsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_concerts_fav_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val favourite_artist = findViewById<Button>(R.id.ArtistasFavoritosConcertIcon1)
//        favourite_artist.setOnClickListener {
//            val intent = Intent(this, ConcertDetailsActivity::class.java)
//            startActivity(intent)
//        }
    }
}