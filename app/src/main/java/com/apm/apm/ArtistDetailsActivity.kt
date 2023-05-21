package com.apm.apm

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
import com.apm.apm.objects.Concert
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await


internal class ArtistDetailsActivity : GetNavigationBarActivity() {
    lateinit var favArtists: ArrayList<HashMap<String, String>>
    private lateinit var favButton: ImageButton

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
        favButton = findViewById<ImageButton>(R.id.favButton)

        val query = intent.getStringExtra("query")
        if (query != null) {
            val url = "attractions?apikey=$apikey&keyword=$query"
            lifecycleScope.launch {
                //val artistResponse = artistService.getArtistDetails(url)
                val call = artistService.getArtistDetails(url)
                val response: ArtistResponse? = call.body()
                if (call.isSuccessful && response != null) {
                    if (!response.embeddedArtists?.attractions.isNullOrEmpty()) {
                        val artist = ArtistMapper().ArtistResponseToArtist(response)
                        showArtistDetails(artist)
                        val tabLayout = findViewById<TabLayout>(R.id.tabs)
                        val viewPager = findViewById<ViewPager>(R.id.viewPager)
                        viewPager.adapter =
                            TabAdapter(supportFragmentManager, artist.artistId, artist.completeName)
                        tabLayout.setupWithViewPager(viewPager)
                        setupFavoriteButton(artist)
                        /*favButton.setOnClickListener {
                            //var isFavorite = isArtistFavorite(artist.artistId)
                            val isFavorite = lifecycleScope.async { isArtistFavorite(artist.artistId) }.await()
                            println(isFavorite)
                            if (isFavorite) {
                                // El artista está en la lista de favoritos
                                // Eliminar el artista de la base de datos
                                deleteArtistFromFavorites(artist.artistId)
                            } else {
                                // El artista no está en la lista de favoritos
                                // Agregar el artista a la base de datos
                                uploadDB(artist.artistId, artist.completeName)
                                Toast.makeText(
                                    applicationContext,
                                    "Artista añadido a favoritos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        // Actualizar el estado del botón al cargar la página
                        val isFavorite = isArtistFavorite(artist.artistId)
                        updateFavoriteButtonState(isFavorite)*/
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


    private suspend fun setupFavoriteButton(artist: Artist) {
        val isFavorite = isArtistFavorite(artist.artistId)
        println(isFavorite)
        favButton.setOnClickListener {
            lifecycleScope.launch {
                val isFavorite = isArtistFavorite(artist.artistId)
                if (isFavorite) {
                    deleteArtistFromFavorites(artist.artistId)
                } else {
                    uploadDB(artist.artistId, artist.completeName)
                    Toast.makeText(
                        applicationContext,
                        "Artista añadido a favoritos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
        updateFavoriteButtonState(true)
    }

    //funcion para comprobar si el artista esta en favoritos y pintar el favButton como corresponda
    private suspend fun isArtistFavorite(artistId: String): Boolean {
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        val db = Firebase.firestore
        val docRef = db.collection("users").document(uid ?: "")

        return try {
            val document = docRef.get().await()
            if (document != null && document.exists()) {
                val favArtists = document.get("favArtists") as? ArrayList<HashMap<String, String>>
                favArtists != null && favArtists.any { it["artistId"] == artistId }
            } else {
                false
            }
        } catch (exception: Exception) {
            println("Error al obtener los datos del usuario: $exception")
            false
        }
    }

    private fun updateFavoriteButtonState(isFavorite: Boolean) {
        if (isFavorite) {
            favButton.setColorFilter(Color.YELLOW)
        } else {
            favButton.clearColorFilter()
        }
    }

    private fun deleteArtistFromFavorites(artistId: String) {
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        val db = Firebase.firestore
        val docRef = db.collection("users").document(uid ?: "")

        docRef.update("favArtists", FieldValue.arrayRemove(getFavoriteArtistMap(artistId)))
            .addOnSuccessListener {
                Toast.makeText(
                    applicationContext,
                    "Artista eliminado de favoritos",
                    Toast.LENGTH_SHORT
                ).show()
                updateFavoriteButtonState(false)
            }
            .addOnFailureListener { exception ->
                println("Error al eliminar el artista de favoritos: $exception")
            }
    }

    private fun getFavoriteArtistMap(artistId: String): Map<String, String> {
        return mapOf(
            "artistId" to artistId
        )
    }
}
