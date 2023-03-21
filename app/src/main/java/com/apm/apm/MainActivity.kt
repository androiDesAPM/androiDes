package com.apm.apm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

//        //TODO Con esto se podría llamar a otra pantalla o actividad con un boton. Usar algo asi para esta semana
//        val button = findViewById<Button>(R.id.button)
//        button.setOnClickListener {
//            val intent = Intent(this, MapActivity::class.java)
//            startActivity(intent)
//        }

        //TODO Creo que aquí tendría que ir la inicialización de la barra del nav_menu. No creo que tengas q meterlo cada vez por Activity
    }
}