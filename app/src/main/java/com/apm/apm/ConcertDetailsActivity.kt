package com.apm.apm
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConcertDetailsActivity : GetNavigationBarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_concert)

        val concertButton = findViewById<Button>(R.id.buy_tickets)
        concertButton.setOnClickListener {
            val intent = Intent(this, BuyTicketsActivity::class.java)
            startActivity(intent)
        }

        //Creamos la barra inferior
        this.getNavigationView()

    }
}