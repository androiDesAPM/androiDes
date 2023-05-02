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
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.mappers.ConcertMapper
import com.apm.apm.objects.Concert
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ConcertsFromFavArstistsFragment : Fragment(), LifecycleOwner {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavArtistAdapter
    //Almacena una referencia al job de la corrutina
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()
    private val favArtists = mutableListOf<String>()
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
            //Cojo el dia de hoy y lo formateo para que no aparezcan conciertos pasados en la home
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val formattedDateTime = currentDateTime.format(formatter)
            val apikey = "Uq1UGcBMZRAzE7ydjGBoAfhk8oSMX6lT"
            val baseUrl = "events"
            //Esta lista mas adelante se sacara de la base de datos
            favArtists.addAll(listOf("Bad gyal"))
            val apiService = ApiClient().getRetrofit().create(APIService::class.java)
            //Petición a la API
            for (artist in favArtists) {
            val url = "$baseUrl?apikey=$apikey&startDateTime=$formattedDateTime&keywords=$artist"
            val call = apiService.getFavArtistsConcerts(url)
            val response: ConcertsResponse? = call.body()
            if (call.isSuccessful && response != null)  {
                val concertsApi = ConcertMapper().ConcertsResponseToConcerts(response)
                concerts.addAll(concertsApi)
            }
        }
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