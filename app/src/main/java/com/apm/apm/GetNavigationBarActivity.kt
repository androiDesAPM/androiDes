package com.apm.apm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class GetNavigationBarActivity : AppCompatActivity(){

    fun getNavigationView(): BottomNavigationView{
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
        return bottomNavigationView
    }
}