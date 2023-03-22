package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyProfile : GetNavigationBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myprofile)

        val ticketsButton = findViewById<Button>(R.id.ticketsButton)
        ticketsButton.setOnClickListener {
            val intent = Intent(this, MyTicketsActivity::class.java)
            startActivity(intent)
        }

        val walletButton = findViewById<Button>(R.id.walletButton)
        walletButton.setOnClickListener {
            val intent = Intent(this, MyCoinsActivity::class.java)
            startActivity(intent)
        }

        //Creamos la barra inferior
        this.getNavigationView()
    }
}