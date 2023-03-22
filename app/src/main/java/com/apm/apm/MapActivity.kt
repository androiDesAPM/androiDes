package com.apm.apm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapActivity : GetNavigationBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        //Creamos la barra inferior
        this.getNavigationView()




        //TODO igual meter que aparezca algo al updatear el buscador aunque el mapa no se vea todavía
//        mActivityName = getString(R.string.activity_b_label)
//        mStatusView = findViewById<View>(R.id.status_view_b) as TextView
//        mStatusAllView = findViewById<View>(R.id.status_view_all_b) as TextView
//        mStatusTracker.setStatus(mActivityName!!, getString(R.string.on_create))
//        printStatus(mStatusView, mStatusAllView)
    }
}