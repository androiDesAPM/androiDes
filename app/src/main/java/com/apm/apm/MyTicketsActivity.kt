package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyTicketsActivity : GetNavigationBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_tickets)

//        val ticketBox1 = findViewById<ConstraintLayout>(R.id.ticketBox1)
//        ticketBox1.setOnClickListener {
//            Toast.makeText(this, "No hay tickets disponibles", Toast.LENGTH_SHORT).show()
//        }

//        val button = findViewById<ImageButton>(R.id.imageButton8_2)
//        button.setOnClickListener {
//            val intent = Intent(this, MapActivity::class.java)
//            startActivity(intent)
//        }


        //Creamos la barra inferior
        this.getNavigationView()

    }
}