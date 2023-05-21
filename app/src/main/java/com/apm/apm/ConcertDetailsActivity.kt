package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.apm.apm.objects.Concert
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ConcertDetailsActivity : GetNavigationBarActivity() {
    lateinit var tickets: ArrayList<HashMap<String, String>>
    private var concert: Concert? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_concert)
        tickets = ArrayList()
        concert = intent.getParcelableExtra("concert")
        lifecycleScope.launch {
            concert?.let { showConcertDetails(it) }
        }
        val concertButton = findViewById<Button>(R.id.buy_tickets)
        concertButton.setOnClickListener {
            uploadDB(concert)
            val intent = Intent(this, BuyTicketsActivity::class.java)
            startActivity(intent)
        }

        //Creamos la barra inferior
        this.getNavigationView()

    }

    private fun uploadDB(concert: Concert?) {
        val db = Firebase.firestore
        val concertTicket = hashMapOf(
            "concertLocationName" to concert?.concertLocationName,
            "concertDate" to concert?.concertDate?.toString(),
            "concertArtistName" to concert?.concertArtistName
        )

        val user = Firebase.auth.currentUser
        val email = user?.email

        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.data["email"] == email) {
                    val id = document.id
                    db.collection("users").document(id)
                        .update("tickets", FieldValue.arrayUnion(concertTicket))
                }
            }
        }
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