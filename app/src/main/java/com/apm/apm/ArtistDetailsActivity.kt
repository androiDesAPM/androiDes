package com.apm.apm

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.apm.apm.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout


internal class ArtistDetailsActivity : GetNavigationBarActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_details)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)

        viewPager.adapter = TabAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        val favButton = findViewById<ImageButton>(R.id.favButton)
        favButton.setOnClickListener {
            Toast.makeText(this, "Artista añadido a favoritos", Toast.LENGTH_SHORT).show()
        }

        //Creamos la barra inferior
        this.getNavigationView()
    }
}