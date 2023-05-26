package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.apm.apm.adapter.TabAdapter
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.data.ArtistSpotifyResponse
import com.apm.apm.data.ArtistTicketMasterResponse
import com.apm.apm.data.SpotifyTokenResponse
import com.apm.apm.mappers.ArtistSpotifyMapper
import com.apm.apm.mappers.ArtistTicketMasterMapper
import com.apm.apm.objects.Artist
import com.apm.apm.util.SpotifyUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

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
                var result = false
                //Check if the artist exists
                val artistService = ApiClient().getSpotifyData().create(APIService::class.java)

                lifecycleScope.launch {
                    val token = SpotifyUtil().authorizeSpotify()

                    //Comprobamos si el artista existe
                    val call = artistService.getSpotifyArtistByName("search?q=$query&type=artist&limit=1", "Bearer "+token)
                    val response: ArtistSpotifyResponse? = call.body()

                    if (call.isSuccessful && response != null && (response.artists.items).isNotEmpty()) {
                        val intent = Intent(applicationContext, ArtistDetailsActivity::class.java)
                        intent.putExtra("query", query)
                        startActivity(intent)
                        result = true
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "No se ha encontrado ningún artista con ese nombre, inténtalo de nuevo",
                            Toast.LENGTH_SHORT
                        ).show()
                        result = false
                    }
                }
                result
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

        // Recargar la página de inicio
        recreate()
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}