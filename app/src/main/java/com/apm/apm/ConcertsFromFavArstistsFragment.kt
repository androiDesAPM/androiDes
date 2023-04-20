package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.FavArtistAdapter
import com.apm.apm.objects.Concert
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

class ConcertsFromFavArstistsFragment : Fragment(), LifecycleOwner {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavArtistAdapter
    //Almacena una referencia al job de la corrutina
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_concerts_fav_artists, container, false)

        recyclerView = view.findViewById(R.id.listConcertsFavArtistsRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapter = FavArtistAdapter(concerts)
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val progressBar: ProgressBar = view.findViewById(R.id.progressbar)
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            getConcertsCorrutine(progressBar)

        }
    }

    private fun getConcertsCorrutine(progressBar: ProgressBar) {
        job = lifecycleScope.launch {
            delay(5000L) // delay non bloqueante (do thread actual) de 1000 milisegundos

            //Petición a la API

            //Añadir los conciertos encontrados
            concerts.add(Concert("Lugar concierto", LocalDate.now(), "Rosalía"))
            concerts.add(Concert("Lugar concierto2", LocalDate.now(), "Bad Gyal"))
            concerts.add(Concert("Lugar concierto3", LocalDate.now(), "nombreArtista3"))
            concerts.add(Concert("Lugar concierto4", LocalDate.now(), "nombreArtista4"))
            concerts.add(Concert("Lugar concierto5", LocalDate.now(), "nombreArtista5"))

            adapter.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE
        }

        progressBar.visibility = View.VISIBLE

        println("Cargando conciertos ....") // o thread principal continúa durante o delay da corutina
        //Thread.sleep(5000L) // bloquéase o thread actual durante dous segundos
    }


    override fun onDestroy() {
        //cancela la corrutina
        super.onDestroy()
        job.cancel()
    }
}