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
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.mappers.ConcertMapper
import com.apm.apm.mappers.GenreMapper
import com.apm.apm.objects.Concert
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConcertsFromFavGenresFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavGenresAdapter
    //Almacena una referencia al job de la corrutina
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()
    private val favGenres = mutableListOf<String>()
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
            delay(5000L) // delay non bloqueante (do thread actual) de 1000 milisegundos
            //Cojo el dia de hoy y lo formateo para que no aparezcan conciertos pasados en la home
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val formattedDateTime = currentDateTime.format(formatter)
            val apikey = "Uq1UGcBMZRAzE7ydjGBoAfhk8oSMX6lT"
            val baseUrl = "events"
            //Prueba para ver si saca bien los generos
            /*val musicSegmentId = "KZFzniwnSyZfZ7v7nJ"
            val apiServiceMusic = ApiClient().getRetrofitMusicGenres().create(APIService::class.java)
            val urlMusic = "$musicSegmentId?apikey=$apikey"
            val callMusic = apiServiceMusic.getMusicGenres(urlMusic)
            val musicResponse = callMusic.body()
            if (callMusic.isSuccessful && musicResponse != null)  {
                val genres = GenreMapper().MusicResponseToGenre(musicResponse)
            }*/
            // id del segmento Musica KZFzniwnSyZfZ7v7nJ, dentro del segmento hay generos y dentro de estos subgeneros
            // id del genero pop KnvZfZ7vAev
            // id del subgenero kpop dentro de pop KZazBEonSMnZfZ7vkE1, esto hay que cambiarlo luego
            favGenres.addAll(listOf("KZazBEonSMnZfZ7vkE1"))
            val apiService = ApiClient().getRetrofit().create(APIService::class.java)
            //Petición a la API
            for (genre in favGenres) {
                val url = "$baseUrl?apikey=$apikey&startDateTime=$formattedDateTime&subGenreId=$genre"
                val call = apiService.getFavArtistsConcerts(url)
                val response = call.body()
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