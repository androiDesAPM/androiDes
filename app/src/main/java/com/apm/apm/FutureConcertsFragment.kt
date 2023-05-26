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
import com.apm.apm.adapter.FutureConcertsAdapter
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.api.ArtistService
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.mappers.ConcertMapper
import com.apm.apm.objects.Concert
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FutureConcertsFragment : Fragment(), LifecycleOwner {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FutureConcertsAdapter
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()

    companion object {
        fun newInstance(ticketMasterId: String): FutureConcertsFragment {
            val fragment = FutureConcertsFragment()
            val args = Bundle().apply {
                putString("ticketMasterId", ticketMasterId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewFragment =
            inflater.inflate(R.layout.fragment_future_concerts_list, container, false);

        recyclerView = viewFragment.findViewById(R.id.listFutureConcertsRecyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = FutureConcertsAdapter(concerts)
        recyclerView.adapter = adapter

        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val progressBar: ProgressBar = view.findViewById(R.id.progressBarFutureConcerts)
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            getFutureConcertsCorrutine(progressBar)
        }
    }

    private fun getFutureConcertsCorrutine(progressBar: ProgressBar) {
        job = lifecycleScope.launch {

            //Get artist id in TicketMaster
            var ticketMasterId = arguments?.getString("ticketMasterId")

            if (ticketMasterId != null && ticketMasterId != "") {
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val formattedDateTime = currentDateTime.format(formatter)
                val apikey = "Uq1UGcBMZRAzE7ydjGBoAfhk8oSMX6lT"
                val baseUrl = "events"
                val apiService = ApiClient().getRetrofit().create(APIService::class.java)
                //Petición a la API
                val url = "$baseUrl?apikey=$apikey&startDateTime=$formattedDateTime&attractionId=$ticketMasterId"
                val call = apiService.getFavArtistsConcerts(url)
                val response: ConcertsResponse? = call.body()
                //comprobar si devuelve lista vacia
                if (call.isSuccessful && response != null) {
                    if (!response.embedded?.events.isNullOrEmpty()) {
                        val concertsApi = ConcertMapper().ConcertsResponseToConcerts(response)
                        concerts.addAll(concertsApi)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No se encontraron conciertos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "No se encontraron conciertos", Toast.LENGTH_LONG)
                        .show()
                }

                adapter.notifyDataSetChanged()
                progressBar.visibility = View.INVISIBLE
            }
            else {
                Toast.makeText(
                    requireContext(),
                    "No se han podido recuperar los conciertos del artista",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
