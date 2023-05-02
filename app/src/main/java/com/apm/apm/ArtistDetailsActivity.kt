package com.apm.apm

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


internal class ArtistDetailsActivity : GetNavigationBarActivity(){
    private val artistService = Retrofit.Builder()
        .baseUrl("https://app.ticketmaster.com/discovery/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ArtistService::class.java)
    //Este artist despues se supone que vendra de la barra de búsqueda o sino el Id desde ver un concierto
    val artist = "Imagine Dragons"
    val apikey = "Uq1UGcBMZRAzE7ydjGBoAfhk8oSMX6lT"
    val url = "attractions?apikey=$apikey&keyword=$artist"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_details)

        lifecycleScope.launch {
                //val artistResponse = artistService.getArtistDetails(url)
                val call = artistService.getArtistDetails(url)
                val response: ArtistResponse? = call.body()
                println(response)
                if (call.isSuccessful && response != null) {
                    println("Call fue bien")
                    val artist = ArtistMapper().ArtistResponseToArtist(response)
                    showArtistDetails(artist)
                    println("artist")
                    val tabLayout = findViewById<TabLayout>(R.id.tabs)
                    val viewPager = findViewById<ViewPager>(R.id.viewPager)
                    viewPager.adapter = TabAdapter(supportFragmentManager)
                    tabLayout.setupWithViewPager(viewPager)
                }
        }

        val favButton = findViewById<ImageButton>(R.id.favButton)
        favButton.setOnClickListener {
            Toast.makeText(this, "Artista añadido a favoritos", Toast.LENGTH_SHORT).show()
        }

        //Creamos la barra inferior
        this.getNavigationView()
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

}