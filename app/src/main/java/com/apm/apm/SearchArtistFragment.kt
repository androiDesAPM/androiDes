package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class SearchArtistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lv = view.findViewById<ListView>(R.id.listViewArtists)
        val items = listOf("Cantante 1", "Cantante 2", "Cantante 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        lv.adapter = adapter
        lv.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), ArtistDetailsActivity::class.java)
            intent.putExtra("selected_item", items[position])
            startActivity(intent)
        }
    }
}