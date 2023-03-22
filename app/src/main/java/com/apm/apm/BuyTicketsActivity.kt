package com.apm.apm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class BuyTicketsActivity : GetNavigationBarActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buy_ticket)

        //Creamos la barra inferior
        this.getNavigationView()
    }
}