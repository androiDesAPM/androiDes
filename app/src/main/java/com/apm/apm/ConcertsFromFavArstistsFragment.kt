package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.FavArtistAdapter
import com.apm.apm.objects.Concert
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

class ConcertsFromFavArstistsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavArtistAdapter

//    val concerts = listOf(
//        Concert("Lugar concierto", LocalDate.now(),"nombreArtista"),
//        Concert("Lugar concierto", LocalDate.now(),"nombreArtista"),
//        Concert("Lugar concierto", LocalDate.now(),"nombreArtista")
//    )
    val concerts = mutableListOf<Concert>()

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

        val progressBar: ProgressBar = view.findViewById(R.id.progressbar)
        progressBar.visibility = View.VISIBLE

        getConcertsCorrutine(progressBar)
//        val layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//
//        val myList = view.findViewById(R.id.listConcertsFavArtistsRecycleView) as RecyclerView
//        myList.layoutManager = layoutManager
    }

    private fun getConcertsCorrutine(progressBar: ProgressBar) {
        GlobalScope.launch { // créase unha nova corrutina en segundo plano
            delay(2000L) // delay non bloqueante (do thread actual) de 1000 milisegundos

            //Petición a la API

            //Añadir los conciertos encontrados
            concerts.add(Concert("Lugar concierto", LocalDate.now(),"Rosalía"))
            concerts.add(Concert("Lugar concierto2", LocalDate.now(),"Bad Gyal"))
            concerts.add(Concert("Lugar concierto2", LocalDate.now(),"nombreArtista2"))

            progressBar.visibility = View.INVISIBLE
        }

        progressBar.visibility = View.VISIBLE

        println("Cargando conciertos ....") // o thread principal continúa durante o delay da corutina
        Thread.sleep(5000L) // bloquéase o thread actual durante dous segundos
    }

}