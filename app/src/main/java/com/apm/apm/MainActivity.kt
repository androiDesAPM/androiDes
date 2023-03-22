package com.apm.apm

import android.os.Bundle

class MainActivity : GetNavigationBarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set de la vista
        setContentView(R.layout.home_page)

        //Creamos la barra inferior
        this.getNavigationView()

    }
}