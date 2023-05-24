package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
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

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchEditText.requestFocus()
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString()
                val intent = Intent(this, ArtistDetailsActivity::class.java)
                intent.putExtra("query", query)
                startActivity(intent)
                true
            } else {
                false

            }
        }

        val clearButton = findViewById<Button>(R.id.clear_button)
        clearButton.setOnClickListener {
            searchEditText.setText("")
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