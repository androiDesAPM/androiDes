package com.apm.apm

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.apm.apm.adapter.TabAdapter
import com.apm.apm.api.ArtistService
import com.apm.apm.data.ArtistResponse
import com.apm.apm.mappers.ArtistMapper
import com.apm.apm.objects.Artist
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await


internal class ArtistDetailsActivity : GetNavigationBarActivity() {
    lateinit var favArtists: ArrayList<HashMap<String, String>>
    private lateinit var favButton: ImageButton
    private lateinit var artist: Artist

    private val artistService = Retrofit.Builder()
        .baseUrl("https://app.ticketmaster.com/discovery/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ArtistService::class.java)

    val apikey = "Uq1UGcBMZRAzE7ydjGBoAfhk8oSMX6lT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_details)
        favArtists = ArrayList()
        favButton = findViewById(R.id.favButton)
        var artistPhotosButton = findViewById<ImageView>(R.id.artistPhotos)
        val query = intent.getStringExtra("query")
        if (query != null) {
            val url = "attractions?apikey=$apikey&keyword=$query"
            lifecycleScope.launch {
                //val artistResponse = artistService.getArtistDetails(url)
                val call = artistService.getArtistDetails(url)
                val response: ArtistResponse? = call.body()
                if (call.isSuccessful && response != null) {
                    if (!response.embeddedArtists?.attractions.isNullOrEmpty()) {
                        artist = ArtistMapper().ArtistResponseToArtist(response)
                        showArtistDetails(artist)
                        val tabLayout = findViewById<TabLayout>(R.id.tabs)
                        val viewPager = findViewById<ViewPager>(R.id.viewPager)
                        viewPager.adapter =
                            TabAdapter(supportFragmentManager, artist.artistId, artist.completeName)
                        tabLayout.setupWithViewPager(viewPager)
                        setupFavoriteButton()
                        artistPhotosButton.setOnClickListener {
                            val intent = Intent(applicationContext, ArtistPhotosActivity::class.java)
                            intent.putExtra("artistId", artist.artistId)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "No se ha encontrado ningún artista con ese nombre",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "No se ha encontrado ningún artista con ese nombre",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        //Creamos la barra inferior
        this.getNavigationView()
    }

    private suspend fun setupFavoriteButton() {
        var isFavorite = lifecycleScope.async { isArtistFavorite(artist.artistId) }.await()
        favButton.setOnClickListener {
            if (isFavorite) {
                lifecycleScope.async { deleteArtistFromFavorites(artist.artistId)}
                isFavorite = false
                updateFavoriteButtonState(isFavorite)
            } else {
                uploadDB(artist.artistId, artist.completeName)
                Toast.makeText(
                    applicationContext,
                    "Artista añadido a favoritos",
                    Toast.LENGTH_SHORT
                ).show()
                isFavorite = true
                updateFavoriteButtonState(isFavorite)
            }
        }
        updateFavoriteButtonState(isFavorite)
    }

    private fun showArtistDetails(artist: Artist) {
        val artistImageView = findViewById<ImageView>(R.id.imageView2)
        val artistNameTextView = findViewById<TextView>(R.id.textView2)
        val artistGenreTextView = findViewById<TextView>(R.id.textView4)

        artistNameTextView.text = artist.completeName
        artistGenreTextView.text = artist.genreName

        Picasso.get()
            .load(artist.imageUrl)
            .fit()
            .centerCrop()
            .into(artistImageView)
    }

    private fun uploadDB(artistId: String, artistName: String) {
        val db = Firebase.firestore
        val favArtist = hashMapOf(
            "artistId" to artistId,
            "artistName" to artistName,
        )

        val user = Firebase.auth.currentUser
        val uid = user?.uid

        db.collection("users").document(uid ?: "")
            .update("favArtists", FieldValue.arrayUnion(favArtist))
    }

    private fun deleteArtistFromFavorites(artistId: String) {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        val uid = user?.uid

        db.collection("users").document(uid ?: "").get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val favArtists = document.get("favArtists") as? ArrayList<HashMap<String, String>>
                if (favArtists != null) {
                    val iterator = favArtists.iterator()
                    while (iterator.hasNext()) {
                        val artist = iterator.next()
                        if (artist["artistId"] == artistId) {
                            iterator.remove()
                        }
                    }
                    db.collection("users").document(uid ?: "")
                        .update("favArtists", favArtists)
                }
            }
        }
    }

    //funcion para comprobar si el artista esta en favoritos y pintar el favButton como corresponda
    private suspend fun isArtistFavorite(artistId: String): Boolean {
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        val db = Firebase.firestore
        var retval = false

        db.collection("users").document(uid ?: "").get().await().let {
            document ->
            if (document != null && document.exists()) {
                val favArtists = document.get("favArtists") as? ArrayList<HashMap<String, String>>
                if (favArtists != null) {
                    retval = favArtists.any { it["artistId"] == artistId }
                }
            }
        }
        return retval
    }

    private fun updateFavoriteButtonState(isFavorite: Boolean) {
        if (isFavorite) {
            favButton.setColorFilter(Color.YELLOW)
        } else {
            favButton.clearColorFilter()
        }
    }
}
