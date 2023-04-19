package com.apm.apm

import android.os.Bundle


internal class ArtistDetailsPastActivity : GetNavigationBarActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_details_past)

        this.getNavigationView()
    }
}