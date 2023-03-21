package com.apm.apm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_tickets)

//        //TODO Con esto se podría llamar a otra pantalla o actividad con un boton. Usar algo asi para esta semana
        val button = findViewById<ImageButton>(R.id.imageButton8_2)
        button.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<ImageButton>(R.id.imageButton8)
        button2.setOnClickListener {
            Toast.makeText(this, "No hay tickets disponibles", Toast.LENGTH_SHORT).show()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.navigation_map -> {
                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                }
                R.id.navigation_user -> {
                    val intent = Intent(this, MyProfile::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        //TODO Creo que aquí tendría que ir la inicialización de la barra del nav_menu. No creo que tengas q meterlo cada vez por Activity
    }

}