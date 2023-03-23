package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast

class MainActivity : GetNavigationBarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set de la vista
        setContentView(R.layout.home_page)

        //Funcionalidad para el buscador del artista
        val searchView = findViewById<SearchView>(R.id.searchArtistHomeView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, "Busqueda del artista $query", Toast.LENGTH_LONG)
                    .show()
                return true
            }

            //TODO borrar esto si al final no metemos sugerencias
            override fun onQueryTextChange(newText: String): Boolean {
                // Escribir aqui el código de las sugerencias para cuando el usuario este escribiendo
                // Habría que poner algo como app:querySuggestionEnabled="true" en el xml
                return false
            }
        })

        val favourite_artist = findViewById<Button>(R.id.ArtistasFavoritosConcertIcon1)
        favourite_artist.setOnClickListener {
            val intent = Intent(this, ArtistDetailsActivity::class.java)
            startActivity(intent)
        }

        //Creamos la barra inferior
        this.getNavigationView()



    }
}