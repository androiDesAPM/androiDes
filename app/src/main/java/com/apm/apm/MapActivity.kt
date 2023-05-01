package com.apm.apm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapActivity : GetNavigationBarActivity(), OnMapReadyCallback {

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        val location = LocationServices.getFusedLocationProviderClient(this)
        location.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }
    }
}