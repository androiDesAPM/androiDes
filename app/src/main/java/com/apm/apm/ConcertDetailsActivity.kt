package com.apm.apm
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.apm.apm.objects.Artist
import com.apm.apm.objects.Concert
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ConcertDetailsActivity : GetNavigationBarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_concert)

        val concert: Concert? = intent.getParcelableExtra("concert")
        lifecycleScope.launch {
            if (concert != null){
                showConcertDetails(concert)
            }
        }
        val concertButton = findViewById<Button>(R.id.buy_tickets)
        concertButton.setOnClickListener {
            val intent = Intent(this, BuyTicketsActivity::class.java)
            startActivity(intent)
        }

        //Creamos la barra inferior
        this.getNavigationView()

    }


    private fun showConcertDetails(concert: Concert) {
        val artistImageView = findViewById<ImageView>(R.id.imageView3)
        val artistNameTextView = findViewById<TextView>(R.id.textView10)
        val concertLocation = findViewById<TextView>(R.id.textView11)
        val concertDate = findViewById<TextView>(R.id.textView12)
        //estos faltan
        val concertStreet = findViewById<TextView>(R.id.textView13)
        val concertVenue = findViewById<TextView>(R.id.textView14)
        //precio iria aqui tambien?

        artistNameTextView.text = concert.concertArtistName
        concertLocation.text = concert.concertLocationName
        concertDate.text = concert.concertDate.toString()

        Picasso.get()
            .load(concert.imageUrl)
            .fit()
            .centerCrop()
            .into(artistImageView)

    }
}