package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyProfile : AppCompatActivity() {

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
    }
}