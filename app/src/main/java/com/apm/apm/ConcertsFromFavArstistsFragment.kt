package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.FavArtistAdapter
import com.apm.apm.objects.Concert
import java.time.LocalDate

class ConcertsFromFavArstistsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavArtistAdapter

    val concerts = listOf(
        Concert("Lugar concierto", LocalDate.now(),"nombreArtista"),
        Concert("Lugar concierto", LocalDate.now(),"nombreArtista"),
        Concert("Lugar concierto", LocalDate.now(),"nombreArtista")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_concerts_fav_artists, container, false)

        val view = inflater.inflate(R.layout.fragment_concerts_fav_artists, container, false)

        recyclerView = view.findViewById(R.id.listConcertsFavArtistsRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        adapter = FavArtistAdapter(concerts)
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

//        val layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//
//        val myList = view.findViewById(R.id.listConcertsFavArtistsRecycleView) as RecyclerView
//        myList.layoutManager = layoutManager
    }
}