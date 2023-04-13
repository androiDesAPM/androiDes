package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ConcertsFromNearUbicationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_concerts_near_ubication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nearYouButton = view.findViewById<Button>(R.id.GenerosCercaDeTi)
        nearYouButton.setOnClickListener {
            val intent = Intent(requireContext(), ConcertDetailsActivity::class.java)
            startActivity(intent)
        }
    }

}