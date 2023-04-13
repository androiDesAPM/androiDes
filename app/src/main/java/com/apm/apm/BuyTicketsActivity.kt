package com.apm.apm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class BuyTicketsActivity : GetNavigationBarActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buy_ticket)

        val fragment = BuyTicketsFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.buyTicketFragmentContainer, fragment)
            .commit()

        //Creamos la barra inferior
        this.getNavigationView()
    }

}