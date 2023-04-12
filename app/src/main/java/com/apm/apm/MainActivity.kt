package com.apm.apm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast

class MainActivity : GetNavigationBarActivity() {

    private lateinit var searchView: SearchView
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var searchArtistFragment: SearchArtistFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set de la vista
        setContentView(R.layout.home_page)

        //Funcionalidad para el buscador del artista
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.searchArtistHomeView)
        fragmentContainer = findViewById(R.id.frameLayoutSearch)
        searchArtistFragment = SearchArtistFragment()

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            //TODO borrar esto si al final no metemos sugerencias
            override fun onQueryTextChange(newText: String): Boolean {
                val isQueryEmpty = newText.isNullOrEmpty()
                if (isQueryEmpty) {
                    supportFragmentManager.beginTransaction()
                        .remove(searchArtistFragment)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutSearch, searchArtistFragment)
                        .addToBackStack(null)
                        .commit()
                }
                return true
            }
        })

        val clearButton = findViewById<Button>(R.id.clear_button)
        clearButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
            searchView.setQuery("", false)
            supportFragmentManager.beginTransaction()
                .remove(searchArtistFragment)
                .commit()
        }


        val favourite_artist = findViewById<Button>(R.id.ArtistasFavoritosConcertIcon1)
        favourite_artist.setOnClickListener {
            val intent = Intent(this, ConcertDetailsActivity::class.java)
            startActivity(intent)
        }

        val favourite_genre = findViewById<Button>(R.id.GenerosFavoritosConcertIcon1)
        favourite_genre.setOnClickListener {
            val intent = Intent(this, ConcertDetailsActivity::class.java)
            startActivity(intent)
        }

        val near_you = findViewById<Button>(R.id.GenerosCercaDeTi)
        near_you.setOnClickListener {
            val intent = Intent(this, ConcertDetailsActivity::class.java)
            startActivity(intent)
        }

        //Creamos la barra inferior
        this.getNavigationView()
    }
}