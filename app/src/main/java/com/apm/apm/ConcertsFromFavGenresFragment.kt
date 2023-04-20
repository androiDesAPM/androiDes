package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.FavGenresAdapter
import com.apm.apm.objects.Concert
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

class ConcertsFromFavGenresFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavGenresAdapter
    //Almacena una referencia al job de la corrutina
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_concerts_fav_genres, container, false)

        recyclerView = view.findViewById(R.id.listConcertsFavGenresRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        adapter = FavGenresAdapter(concerts)
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar: ProgressBar = view.findViewById(R.id.progressbarGenres)
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            getConcertsCorrutine(progressBar)

        }
    }

    private fun getConcertsCorrutine(progressBar: ProgressBar) {
        job = lifecycleScope.launch {
            delay(10000L) // delay non bloqueante (do thread actual) de 1000 milisegundos

            //TODO Petición a la API

            //Añadir los conciertos encontrados
            concerts.add(Concert("Lugar concierto", LocalDate.now(), "Rosalía"))
            concerts.add(Concert("Lugar concierto2", LocalDate.now(), "Bad Gyal"))
            concerts.add(Concert("Lugar concierto3", LocalDate.now(), "nombreArtista3"))
            concerts.add(Concert("Lugar concierto4", LocalDate.now(), "nombreArtista4"))
            concerts.add(Concert("Lugar concierto5", LocalDate.now(), "nombreArtista5"))

            adapter.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE
        }

        //TODO hay que poner esto en otro sitio
        progressBar.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        //cancela la corrutina
        super.onDestroy()
        job.cancel()
    }

}