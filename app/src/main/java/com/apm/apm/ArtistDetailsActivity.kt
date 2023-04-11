package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabItem

internal class ArtistDetailsActivity : GetNavigationBarActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_details)

        val concertButton = findViewById<TextView>(R.id.VerMas1)
        concertButton.setOnClickListener {
            val intent = Intent(this, ConcertDetailsActivity::class.java)
            startActivity(intent)
        }
        val concertButton2 = findViewById<TextView>(R.id.VerMas2)
        concertButton2.setOnClickListener {
            val intent = Intent(this, ConcertDetailsActivity::class.java)
            startActivity(intent)
        }

        val chatButton1 = findViewById<ImageButton>(R.id.chatButton1)
        chatButton1.setOnClickListener {
            Toast.makeText(this, "Aún no se ha creado el chat del concierto", Toast.LENGTH_SHORT).show()
        }
        val chatButton2 = findViewById<ImageButton>(R.id.chatButton2)
        chatButton2.setOnClickListener {
            Toast.makeText(this, "Aún no se ha creado el chat del concierto", Toast.LENGTH_SHORT).show()
        }
        val favButton = findViewById<ImageButton>(R.id.favButton)
        favButton.setOnClickListener {
            Toast.makeText(this, "Artista añadido a favoritos", Toast.LENGTH_SHORT).show()
        }

        //Creamos la barra inferior
        this.getNavigationView()
    }
}