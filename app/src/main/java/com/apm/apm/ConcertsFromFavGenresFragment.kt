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
import com.apm.apm.objects.Concert
import com.apm.apm.objects.ConcertList
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConcertsFromFavGenresFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavGenresAdapter

    //Almacena una referencia al job de la corrutina
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()
    private lateinit var cacheFile: File
    private lateinit var progressBar: ProgressBar
    val db = Firebase.firestore

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
        cacheFile = File(requireContext().cacheDir, "fav_genres_concerts_cache")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressbarGenres)

        if (cacheFile.exists() && cacheFile.length() > 0) {
            val inputStream = FileInputStream(cacheFile)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            // Convertir el contenido en un objeto ConcertsResponse
//            val cachedResponse =
//                Gson().fromJson(stringBuilder.toString(), ConcertsResponse::class.java)
//            val concertsResponseToConcerts =
//                ConcertMapper().ConcertsResponseToConcerts(cachedResponse)
            val cachedResponse =
                Gson().fromJson(stringBuilder.toString(), ConcertList::class.java)
            concerts.addAll(cachedResponse.concertList)
            adapter.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                getConcertsCorrutine(progressBar)

            }
        }
    }

    private fun getConcertsCorrutine(progressBar: ProgressBar) {
        job = lifecycleScope.launch {
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

            val favGenres = ArrayList<String>()

            val user = Firebase.auth.currentUser
            val uid = user?.uid

            val genres = db.collection("users").document(uid ?: "").get().await().data?.get("genres") as ArrayList<HashMap<String, String>>
            for (genre in genres) {
                favGenres.add(genre["value"].toString())
            }

            val apiService = ApiClient().getRetrofit().create(APIService::class.java)
            //Petición a la API
            val listaConciertos: ArrayList<Concert> = ArrayList()
            for (genre in favGenres) {
                val url =
                    "$baseUrl?apikey=$apikey&startDateTime=$formattedDateTime&genreId=$genre"
                val call = apiService.getFavArtistsConcerts(url)
                val response = call.body()
                if (call.isSuccessful && response != null) {
                    val concertsResponseToConcerts =
                        ConcertMapper().ConcertsResponseToConcerts(response)
                    concertsResponseToConcerts.forEach { concert ->
                        listaConciertos.add(concert)
                    }
                    concerts.addAll(concertsResponseToConcerts)
                    //TODO BUG CACHE
                }
            }
            val listaConciertosObj = ConcertList(listaConciertos)
            cacheFile.writeText(Gson().toJson(listaConciertosObj))

            adapter.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE
        }

        progressBar.visibility = View.VISIBLE

        println("Cargando conciertos ....") // o thread principal continúa durante o delay da corutina
        //Thread.sleep(5000L) // bloquéase o thread actual durante dous segundos
    }

    fun refreshData() {
        if (cacheFile.exists()) {
            cacheFile.writeText("")
        }
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            getConcertsCorrutine(progressBar)

        }
    }

    /*override fun onDestroy() {
        //cancela la corrutina
        super.onDestroy()
        job.cancel()
    }*/

}