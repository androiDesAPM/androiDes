package com.apm.apm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

internal class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        //TODO igual meter que aparezca algo al updatear el buscador aunque el mapa no se vea todavía
//        mActivityName = getString(R.string.activity_b_label)
//        mStatusView = findViewById<View>(R.id.status_view_b) as TextView
//        mStatusAllView = findViewById<View>(R.id.status_view_all_b) as TextView
//        mStatusTracker.setStatus(mActivityName!!, getString(R.string.on_create))
//        printStatus(mStatusView, mStatusAllView)
    }
}