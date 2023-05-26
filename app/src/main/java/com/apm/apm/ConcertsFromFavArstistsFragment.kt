package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
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


class ConcertsFromFavArstistsFragment : Fragment(), LifecycleOwner {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavArtistAdapter

    //Almacena una referencia al job de la corrutina
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()

    // private val favArtists = mutableListOf<String>()
    private lateinit var cacheFile: File
    private lateinit var progressBar: ProgressBar
    private var artistsIds = mutableListOf<String>()
    val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_concerts_fav_artists, container, false)

        recyclerView = view.findViewById(R.id.listConcertsFavArtistsRecycleView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapter = FavArtistAdapter(concerts)
        recyclerView.adapter = adapter
        cacheFile = File(requireContext().cacheDir, "fav_artists_concerts_cache")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressbar)
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
            val classificationId = "KZFzniwnSyZfZ7v7nJ"
            val favArtists = getFavArtists()
            val apiService = ApiClient().getRetrofit().create(APIService::class.java)
            if (favArtists.isEmpty()) {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(
                    requireContext(),
                    "Aún no tienes artistas favoritos",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //Petición a la API
                val listaConciertos: ArrayList<Concert> = ArrayList()
                for (artist in favArtists) {
                    println(artist)
                    val url =
                        "$baseUrl?apikey=$apikey&classificationId=$classificationId&startDateTime=$formattedDateTime&attractionId=$artist"
                    val call = apiService.getFavArtistsConcerts(url)
                    val response: ConcertsResponse? = call.body()
                    println(response)
                    if (call.isSuccessful && response != null) {
                        val concertsResponseToConcerts =
                            ConcertMapper().ConcertsResponseToConcerts(response)
                        concertsResponseToConcerts.forEach { concert ->
                            listaConciertos.add(concert)
                        }
                        concerts.addAll(concertsResponseToConcerts)
                        // se guarda la respuesta en cache
                        //TODO BUG CACHE
                    }
                }
                val listaConciertosObj = ConcertList(listaConciertos)
                cacheFile.appendText(Gson().toJson(listaConciertosObj))

                adapter.notifyDataSetChanged()
                progressBar.visibility = View.INVISIBLE
            }
        }

        progressBar.visibility = View.VISIBLE
        println("Cargando conciertos ....") // o thread principal continúa durante o delay da corutina
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

    private suspend fun getFavArtists(): MutableList<String> {
        val user = Firebase.auth.currentUser
        val uid = user?.uid

        val document = db.collection("users").document(uid ?: "").get().await()

        if (document.exists()) {
            val favArtists = document.get("favArtists") as? ArrayList<HashMap<String, String>>
            if (favArtists != null) {
                return favArtists.mapNotNull { it["ticketMasterId"] }.toMutableList()
            }
        }

        return mutableListOf()
    }


}
