package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map

        val latLng = LatLng(36.679582, -5.444791)

        // Un zoom mayor que 13 hace que el emulador falle, pero un valor deseado para
        // callejero es 17 aprox.
        val zoom = 13f
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

        // Colocar un marcador en la misma posición
        map.addMarker(MarkerOptions().position(latLng))

//        googleMap.isMyLocationEnabled = true
    }

    override fun onResume() {
        super.onResume()
        if (!::googleMap.isInitialized) {

            val mapFragment = childFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                // Aquí puedes configurar el mapa, añadir marcadores, etc.
            }
        }
    }

}