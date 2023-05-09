package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.PastConcertsAdapter
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.data.BandsInTownResponse
import com.apm.apm.mappers.ConcertMapper
import com.apm.apm.objects.Concert
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PastConcertsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PastConcertsAdapter
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()

    companion object {
        fun newInstance(artistName: String): PastConcertsFragment {
            val fragment = PastConcertsFragment()
            val args = Bundle().apply {
                putString("artistName", artistName)
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
        val viewFragment = inflater.inflate(R.layout.fragment_past_concerts_list, container, false);

        recyclerView = viewFragment.findViewById(R.id.listPastConcertsRecyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

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

    private fun getPastConcertsCorrutine(progressBar: ProgressBar) {
        job = lifecycleScope.launch {
            delay(2000L) // delay non bloqueante (do thread actual) de 1000 milisegundos
            val artistName = arguments?.getString("artistName")
            val apikey = "2eafd9228a7ac50b936e915fbd48bc45"
            val baseUrl = "events"
            val apiService = ApiClient().getRetrofitBandsInTown().create(APIService::class.java)
            //Petición a la API
            val url = "$artistName/$baseUrl?app_id=$apikey&date=past"
            val call = apiService.getArtistPastConcerts(url)
            val response: List<BandsInTownResponse>? = call.body()
            if (call.isSuccessful && response != null) {
                if (!response.isNullOrEmpty()){
                val concertsApi = ConcertMapper().BandsInTownListResponseToConcerts(response)
                concerts.addAll(concertsApi)
                } else {
                    Toast.makeText(requireContext(),"No se encontraron conciertos", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(),"No se encontraron conciertos", Toast.LENGTH_LONG).show()
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