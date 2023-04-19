package com.apm.apm

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast

class MyTicketsActivity : GetNavigationBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_tickets)

        val three_points_button_1 = findViewById<ImageButton>(R.id.three_points_1)
        three_points_button_1.setOnClickListener {
            Toast.makeText(this, "No hay opciones disponibles", Toast.LENGTH_SHORT).show()
        }

        val three_points_button_2 = findViewById<ImageButton>(R.id.three_points_2)
        three_points_button_2.setOnClickListener {
            Toast.makeText(this, "No hay opciones disponibles", Toast.LENGTH_SHORT).show()
        }

        //Creamos la barra inferior
        this.getNavigationView()

    }
}