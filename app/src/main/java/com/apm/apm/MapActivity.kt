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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException


class MapActivity : GetNavigationBarActivity(), OnMapReadyCallback {

    private lateinit var job: Job
    private lateinit var map: GoogleMap
    private var mapState: Bundle? = null
    private lateinit var mapFragment: SupportMapFragment
    val db = Firebase.firestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        val searchView = findViewById<SearchView>(R.id.searchViewOnMap)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MapActivity, "Busqueda en la ciudad $query", Toast.LENGTH_LONG).show()
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

        mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        MapsInitializer.initialize(applicationContext)

        if (savedInstanceState != null) {
            mapState = savedInstanceState.getBundle("MapViewBundleKey")
        }
        mapFragment.onCreate(savedInstanceState)

        //Creamos la barra inferior
        this.getNavigationView()

    }

    override fun onPause() {
        super.onPause()
        mapFragment.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapFragment.onResume()
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        mapFragment.onSaveInstanceState(outState)
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle: Bundle? = outState?.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState?.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapFragment.onSaveInstanceState(mapViewBundle)
    }
    companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
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
                    //llama a obtener ubicación por defecto
                    val ubicacionLatitudeLongitude= getUbicacionPorDefecto()
                    latitude = ubicacionLatitudeLongitude.first
                    longitude= ubicacionLatitudeLongitude.second
                }
            }else if (location != null) {
                //TODO DEJAR ESTO
//                latitude = location.latitude
//                longitude = location.longitude
                //TODO PRUEBA
                val ubicacionLatitudeLongitude= getUbicacionPorDefecto()
                latitude = ubicacionLatitudeLongitude.first
                longitude= ubicacionLatitudeLongitude.second
            } else {
                //llama a obtener ubicación por defecto
                val ubicacionLatitudeLongitude= getUbicacionPorDefecto()
                latitude = ubicacionLatitudeLongitude.first
                longitude= ubicacionLatitudeLongitude.second
            }
            val latLng = LatLng(latitude, longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

            //Llamamos a la API
//                val geoPoint = GeoPoint(latitude, longitude)
            val geoHash = GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, 8)
            try{
                lifecycleScope.launch {
                    //Añadimos los conciertos cerca del geoHash
                    getConcertsByGeoPoint(map, geoHash)
                    //Añadimos el concierto unico de detalles de concierto
                    if (intent.getBooleanExtra("detallesConcierto", false)){
                        val idTicketMaster = intent.getStringExtra("idEventoTicketMaster")
                        if (idTicketMaster != null){
                            getConcertByTicketMasterId(map,idTicketMaster)
                        }
                    }
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

    private fun getConcertByTicketMasterId(map: GoogleMap, idEventoTicketMaster: String) {
        job = lifecycleScope.launch {
            val apikey = "Uq1UGcBMZRAzE7ydjGBoAfhk8oSMX6lT"
            val baseUrl = "events"
            val apiService = ApiClient().getRetrofit().create(APIService::class.java)

            //Petición a la API
            //events?apikey=uaPXPcRZ4b6cPaGJ0xQUvqw6XDdg9hpA&geoPoint=ezdn8
            val url = "$baseUrl?apikey=$apikey&id=$idEventoTicketMaster"
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


    private fun getUbicacionPorDefecto(): Pair<Double, Double> {
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        var location= "A Coruña"
        var latitude = 0.0
        var longitude = 0.0

        db.collection("users").document(uid ?: "").get().addOnSuccessListener { result ->
            val locationFirebase = result?.data?.get("location") as HashMap<String, String>
            val city = locationFirebase["city"].toString()
            location = city
        }

        val geocoder = Geocoder(this@MapActivity)
        try {
            val addressList = geocoder.getFromLocationName(location, 1)
            if (addressList != null) {
                if (addressList.isNotEmpty()) {
                    val address = addressList[0]
                    latitude = address.latitude
                    longitude = address.longitude
                } else {
                    Toast.makeText(this@MapActivity, "No se encontró la ciudad por defecto", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this@MapActivity, "Error al buscar la ciudad por defecto", Toast.LENGTH_LONG).show()
        }
        return Pair(latitude,longitude)
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
}