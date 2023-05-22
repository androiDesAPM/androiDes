package com.apm.apm

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hsr.geohash.GeoHash
import com.apm.apm.adapter.ConcertsNearYouAdapter
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.mappers.ConcertMapper
import com.apm.apm.objects.Concert
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ConcertsFromNearUbicationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConcertsNearYouAdapter
    private lateinit var job: Job
    private val concerts = mutableListOf<Concert>()
    private val nearConcerts = mutableListOf<String>()
    private lateinit var cacheFile: File
    private lateinit var progressBar: ProgressBar
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
        cacheFile = File(requireContext().cacheDir, "near_concerts_cache")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressbarNearUbication)

        if (cacheFile.exists() && cacheFile.length() > 0) {
            val inputStream = FileInputStream(cacheFile)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            // Convertir el contenido en un objeto ConcertsResponse
            val cachedResponse =
                Gson().fromJson(stringBuilder.toString(), ConcertsResponse::class.java)
            concerts.addAll((ConcertMapper().ConcertsResponseToConcerts(cachedResponse)))
            adapter.notifyDataSetChanged()
            println("esta en cache")
        } else {
            progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                getConcertsCorrutine(progressBar)
            }
        }
    }

    private fun getConcertsCorrutine(progressBar: ProgressBar) {
        job = lifecycleScope.launch {
            delay(5000L) // delay non bloqueante (do thread actual) de 1000 milisegundos

            println("NO esta en cache")
            //Cojo el dia de hoy y lo formateo para que no aparezcan conciertos pasados en la home
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val formattedDateTime = currentDateTime.format(formatter)
            val apikey = "Uq1UGcBMZRAzE7ydjGBoAfhk8oSMX6lT"
            val baseUrl = "events"
            val classificationId = "KZFzniwnSyZfZ7v7nJ"
            //Esta lista mas adelante se sacara de la base de datos

            val geoHash = getGeoHashFromLocation()
//            nearConcerts.addAll(listOf("Bad gyal"))
            val apiService = ApiClient().getRetrofit().create(APIService::class.java)
            //Petición a la API
            val url =
                "$baseUrl?apikey=$apikey&classificationId=$classificationId&startDateTime=$formattedDateTime&geoPoint=$geoHash"
            val call = apiService.getFavArtistsConcerts(url)
            val response = call.body()
            if (call.isSuccessful && response != null) {
                val concertsApi = ConcertMapper().ConcertsResponseToConcerts(response)
                concerts.addAll(concertsApi)
                // se guarda la respuesta en cache
                cacheFile.writeText(Gson().toJson(response))
            }

            adapter.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE
        }
        progressBar.visibility = View.VISIBLE

        println("Cargando conciertos ....") // o thread principal continúa durante o delay da corutina
        //Thread.sleep(5000L) // bloquéase o thread actual durante dous segundos
    }

    private fun getGeoHashFromLocation(): String{
        val client = LocationServices.getFusedLocationProviderClient(this.requireContext())
//        get location inside fragment
        var latitude = 43.358934
        var longitude = -8.412103
        if (checkLocationPermission()) {
//            TODO: No funciona la localización
            client.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location?.latitude!!
                    longitude = location?.longitude!!
                }
                else {
                    latitude = 43.332688
                    longitude = -8.410926
                }
            }
        }
        val geoHash = GeoHash.geoHashStringWithCharacterPrecision(latitude!!, longitude!!, 7)
        return geoHash
    }

    private fun checkLocationPermission(): Boolean  {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return false
        }
    }

    fun refreshData() {
        val cacheFile = File(requireContext().cacheDir, "near_concerts_cache")
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