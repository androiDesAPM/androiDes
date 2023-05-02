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
import com.apm.apm.adapter.ConcertsNearYouAdapter
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.mappers.ConcertMapper
import com.apm.apm.objects.Concert
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConcertsFromNearUbicationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConcertsNearYouAdapter
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()
    private val nearConcerts = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_concerts_near_ubication, container, false)

        recyclerView = view.findViewById(R.id.listConcertsNearUbicationRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        adapter = ConcertsNearYouAdapter(concerts)
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val progressBar: ProgressBar = view.findViewById(R.id.progressbarNearUbication)
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
            nearConcerts.addAll(listOf("Bad gyal"))
            val apiService = ApiClient().getRetrofit().create(APIService::class.java)
            //Petición a la API
            for (artist in nearConcerts) {
                val url = "$baseUrl?apikey=$apikey&startDateTime=$formattedDateTime&keywords=$artist"
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