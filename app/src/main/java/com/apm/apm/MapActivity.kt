package com.apm.apm

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import ch.hsr.geohash.GeoHash
import com.apm.apm.adapter.MarkerInfoWindowAdapter
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.mappers.EventMapper
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException


class MapActivity : GetNavigationBarActivity(), OnMapReadyCallback {

    private lateinit var job: Job
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        val searchView = findViewById<SearchView>(R.id.searchViewOnMap)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MapActivity, "Busqueda del artista $query", Toast.LENGTH_LONG).show()
                moveMapToCity(query)
                return true
            }

            //TODO borrar esto si al final no metemos sugerencias
            override fun onQueryTextChange(newText: String): Boolean {
                // Escribir aqui el código de las sugerencias para cuando el usuario este escribiendo
                // Habría que poner algo como app:querySuggestionEnabled="true" en el xml
                return false
            }
        })

        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        MapsInitializer.initialize(applicationContext)

        //Creamos la barra inferior
        this.getNavigationView()

    }

    override fun onMapReady(map: GoogleMap) {
        this.map=map

        map.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val location = LocationServices.getFusedLocationProviderClient(this)
        location.lastLocation.addOnSuccessListener { location ->
            var latitude: Double
            var longitude: Double
            if (intent.getBooleanExtra("detallesConcierto", false)){//Comprobamos si la localización se pasó desde los detalles del concierto
                latitude = intent.getDoubleExtra("latitude", 0.0)
                longitude = intent.getDoubleExtra("longitude", 0.0)
                if(latitude==0.0 || longitude==0.0){
                    Toast.makeText(this@MapActivity, "El concierto no contiene una ubicación válida", Toast.LENGTH_LONG).show()
                    latitude = 43.358934
                    longitude = -8.412103
                    //TODO llamar a obtener ubicación por defecto
                }
            }else if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            } else {
                latitude = 43.358934
                longitude = -8.412103
                 //TODO llamar a obtener ubicación por defecto
            }
            val latLng = LatLng(latitude, longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

            //Llamamos a la API
//                val geoPoint = GeoPoint(latitude, longitude)
            val geoHash = GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, 8)
            try{
                lifecycleScope.launch {
                    getConcertsByGeoPoint(map, geoHash)
                }
            }catch (e : Exception){
                Log.e("MapActivity" ,"Error en la corrutina")
            }
        }
    }


    private fun getConcertsByGeoPoint(map: GoogleMap, geoHash: String) {
        job = lifecycleScope.launch {
            val apikey = "Uq1UGcBMZRAzE7ydjGBoAfhk8oSMX6lT"
            val baseUrl = "events"
            val classificationId ="KZFzniwnSyZfZ7v7nJ"//Este id pertenece a la clasificacion "Music"
            val apiService = ApiClient().getRetrofit().create(APIService::class.java)

            //Petición a la API
            //events?apikey=uaPXPcRZ4b6cPaGJ0xQUvqw6XDdg9hpA&geoPoint=ezdn8
            val url = "$baseUrl?apikey=$apikey&classificationId=$classificationId&geoPoint=$geoHash"
            val call = apiService.getConcertsByGeoPoint(url)
            val response: ConcertsResponse? = call.body()
            if (call.isSuccessful && response != null)  {

                response.embedded.events.forEach { event ->

                    val concertMapInfo = EventMapper().EventToConcertMapInfo(event)

                    var location = event.embeddedEvent.venue.get(0).location
                    Log.i("MapActivity" ,"Location: $location")

                    val ubication = LatLng(
                        location.latitude.toDouble(),
                        location.longitude.toDouble())

                    val marker = map.addMarker(
                        MarkerOptions()
                            .title(event.name)
                            .position(ubication)
//                            .icon(concertMapInfo)
                    )
                    if (marker != null) {
                        marker.tag = concertMapInfo
                    }
                }
            }
        }
    }

    private fun moveMapToCity(city: String) {
        val geocoder = Geocoder(this@MapActivity)
        try {
            val addressList = geocoder.getFromLocationName(city, 1)
            if (addressList != null) {
                if (addressList.isNotEmpty()) {
                    val address = addressList[0]
                    val latLng = LatLng(address.latitude, address.longitude)

                    // Mover el mapa a las coordenadas especificadas
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))

                    // Volver a asignar los marcadores
                    val geoHash = GeoHash.geoHashStringWithCharacterPrecision(address.latitude, address.longitude, 8)
                    getConcertsByGeoPoint(map, geoHash)
                } else {
                    Toast.makeText(this@MapActivity, "No se encontró la ciudad especificada", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this@MapActivity, "Error al buscar la ciudad", Toast.LENGTH_LONG).show()
        }
    }

//    override fun onGeocode(addresses: MutableList<Address>) {
//        if (addresses != null && addresses.isNotEmpty()) {
//            val address = addresses[0]
//            val latLng = LatLng(address.latitude, address.longitude)
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
//        } else {
//            Toast.makeText(this@MapActivity, "No se encontró la ciudad especificada", Toast.LENGTH_LONG).show()
//        }
//    }

}