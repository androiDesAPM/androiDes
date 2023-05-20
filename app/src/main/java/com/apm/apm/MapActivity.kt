package com.apm.apm

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch



class MapActivity : GetNavigationBarActivity(), OnMapReadyCallback {

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        val searchView = findViewById<SearchView>(R.id.searchViewOnMap)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MapActivity, "Busqueda del artista $query", Toast.LENGTH_LONG).show()
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
            if (location != null) {
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

                //Recorremos los eventos cerca de la ubicación encontrados por el API
                for ( event in response.embedded.events){
                    var location = event.venue.venue.get(0).location
                    Log.i("MapActivity" ,"Location: $location")


//                    Picasso.get().load(event.images[0].url).

//                    val iconGenerator = IconGenerator(context)
//                    iconGenerator.setStyle(IconGenerator.STYLE_GREEN)

//                    val textView = TextView(context).apply {
//                        text = "$title\n$date" // Agrega el título y la fecha separados por un salto de línea
//                        setTextColor(Color.BLACK)
//                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
//                    }


                    //var bitmapImagenArtista = cargarImagenArtistaIcono(event.images[0].url)
//                    var bitmapImagenArtista = R.drawable.avatar_1
//                    try {
//                        bitmapImagenArtista = Picasso.get().load(event.images[0].url).get()
//                    } catch (e: Exception) {
//                        val bitmapImagenArtista = R.drawable.avatar_1
//                        BitmapFactory.decodeResource(resources, bitmapImagenArtista)
//                    }

//                    val markerOptions = MarkerOptions()
//                        .position(latLng)
//                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapImagenArtista))
//                        .anchor(iconGenerator.anchorU, iconGenerator.anchorV) // Ajusta el ancla para centrar el icono
//                    map.addMarker(markerOptions)



                    val ubication = LatLng(
                        location.latitude.toDouble(),
                        location.longitude.toDouble())
                    //Añadimos el marcador
                    map.addMarker(
                        MarkerOptions()
                            .position(ubication)
                            //.icon(BitmapDescriptorFactory.fromBitmap(bitmapImagenArtista))
                            .title(event.name+event.dates.toString())
                    )
                }

//                response.embedded.events.forEach { concertMapInfo ->
//                    val marker = map.addMarker(
//                        MarkerOptions()
//                            .title(concertMapInfo.name)
//                            .position(concertMapInfo.latLng)
//                            .icon(concertMapInfo)
//                    )
            }
        }
    }

    fun cargarImagenArtistaIcono(url: String): Bitmap {
        return try {
            Picasso.get().load(url).get()
        } catch (e: Exception) {
            val imagenIconoDefecto = R.drawable.avatar_1
            BitmapFactory.decodeResource(resources, imagenIconoDefecto)
        }
    }

}