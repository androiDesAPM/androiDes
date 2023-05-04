package com.apm.apm

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

enum class ProviderType {
    BASIC
}

class MainActivity : GetNavigationBarActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var fragmentContainer: FrameLayout
    private lateinit var searchArtistFragment: SearchArtistFragment
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var favArtistConcertsFragment: ConcertsFromFavArstistsFragment
    private lateinit var favGenresConcertsFragment: ConcertsFromFavGenresFragment
    private lateinit var nearConcertsFragment: ConcertsFromNearUbicationFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set de la vista
        setContentView(R.layout.home_page)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)
        favArtistConcertsFragment = ConcertsFromFavArstistsFragment()
        favGenresConcertsFragment = ConcertsFromFavGenresFragment()
        nearConcertsFragment = ConcertsFromNearUbicationFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.listConcertsFavArtists, favArtistConcertsFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.listConcertsFavConcerts, favGenresConcertsFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.listConcertsNearUbication, nearConcertsFragment)
            .commit()

        //Funcionalidad para el buscador del artista
        val searchView =
            findViewById<androidx.appcompat.widget.SearchView>(R.id.searchArtistHomeView)
        fragmentContainer = findViewById(R.id.frameLayoutSearch)
        searchArtistFragment = SearchArtistFragment()

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            //TODO borrar esto si al final no metemos sugerencias
            override fun onQueryTextChange(newText: String): Boolean {
                val isQueryEmpty = newText.isEmpty()
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
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
            searchView.setQuery("", false)
            supportFragmentManager.beginTransaction()
                .remove(searchArtistFragment)
                .commit()
        }

        //Creamos la barra inferior
        this.getNavigationView()
    }

    override fun onRefresh() {
        favArtistConcertsFragment.refreshData()
        favGenresConcertsFragment.refreshData()
        nearConcertsFragment.refreshData()
        swipeRefreshLayout.isRefreshing = false
    }
}