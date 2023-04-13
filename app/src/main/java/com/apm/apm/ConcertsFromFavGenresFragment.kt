package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ConcertsFromFavGenresFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_concerts_fav_genres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val favouriteGenre = view.findViewById<Button>(R.id.GenerosFavoritosConcertIcon1)
        favouriteGenre.setOnClickListener {
            val intent = Intent(requireContext(), ConcertDetailsActivity::class.java)
            startActivity(intent)
        }
    }

}