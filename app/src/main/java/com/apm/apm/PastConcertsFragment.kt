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
import com.apm.apm.adapter.PastConcertsAdapter
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.mappers.ConcertMapper
import com.apm.apm.objects.Concert
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PastConcertsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : PastConcertsAdapter
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_past_concerts_list, container, false);

        recyclerView = viewFragment.findViewById(R.id.listPastConcertsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = PastConcertsAdapter(concerts)
        recyclerView.adapter = adapter

        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val progressBar: ProgressBar = view.findViewById(R.id.progressBarPastConcerts)
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            getPastConcertsCorrutine(progressBar)
        }
    }

    private fun getPastConcertsCorrutine(progressBar : ProgressBar) {
        job = lifecycleScope.launch {
            delay(2000L) // delay non bloqueante (do thread actual) de 1000 milisegundos

            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val formattedDateTime = currentDateTime.format(formatter)
            val apikey = "Uq1UGcBMZRAzE7ydjGBoAfhk8oSMX6lT"
            val baseUrl = "events"
            //Esto despues se mandará automaticamente, es el attractionId
            val artistId = "K8vZ917GSz7"
            val apiService = ApiClient().getRetrofit().create(APIService::class.java)
            //Petición a la API
            val url = "$baseUrl?apikey=$apikey&startDateTime=$formattedDateTime&attractionId=$artistId"
            val call = apiService.getFavArtistsConcerts(url)
            val response: ConcertsResponse? = call.body()
            if (call.isSuccessful && response != null) {
                val concertsApi = ConcertMapper().ConcertsResponseToConcerts(response)
                concerts.addAll(concertsApi)
            }

            adapter.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE
        }

        progressBar.visibility = View.VISIBLE

        println("Cargando conciertos ....") // o thread principal continúa durante o delay da corutina

    }


    override fun onDestroy() {
        //cancela la corrutina
        super.onDestroy()
        job.cancel()
    }
}