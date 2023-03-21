package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

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

        //TODO igual meter que aparezca algo al updatear el buscador aunque el mapa no se vea todavía
//        mActivityName = getString(R.string.activity_b_label)
//        mStatusView = findViewById<View>(R.id.status_view_b) as TextView
//        mStatusAllView = findViewById<View>(R.id.status_view_all_b) as TextView
//        mStatusTracker.setStatus(mActivityName!!, getString(R.string.on_create))
//        printStatus(mStatusView, mStatusAllView)
    }
}