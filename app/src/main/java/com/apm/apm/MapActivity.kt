package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapActivity : GetNavigationBarActivity() {

    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        val searchView = findViewById<SearchView>(R.id.searchViewOnMap)

        mapFragment = MapFragment()

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


        //Creamos la barra inferior
        this.getNavigationView()

    }
}