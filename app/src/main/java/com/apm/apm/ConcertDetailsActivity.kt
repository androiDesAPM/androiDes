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

        val mapButton = findViewById<Button>(R.id.concertMapButton)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            //TODO asignar aquí las variables de donde esta el concierto
            val latitude = concert?.concertLatitude?.toDouble()
            val longitude = concert?.concertLongitude?.toDouble()
            val detallesConcierto = true
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("detallesConcierto", detallesConcierto)
            intent.putExtra("idEventoTicketMaster", concert?.ticketMasterEventId)
            startActivity(intent)
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
        val concertLocation = findViewById<TextView>(R.id.textView15)
        val concertDate = findViewById<TextView>(R.id.textView12)
        val concertCity = findViewById<TextView>(R.id.textView13)
        val concertAddress = findViewById<TextView>(R.id.textView14)
        val concertPrice = findViewById<TextView>(R.id.textView16)
        val price = concert.price.toString()
        val currency = concert.currency.toString()

        artistNameTextView.text = concert.concertArtistName
        concertLocation.text = concert.concertLocationName
        concertDate.text = concert.concertDate.toString()
        concertCity.text = concert.concertCity.toString() + ", " + concert.concertState.toString()
        concertAddress.text = concert.concertAddress.toString()
        concertPrice.text = price.plus(currency)

        Picasso.get()
            .load(concert.imageUrl)
            .fit()
            .centerCrop()
            .into(artistImageView)

    }
}