package com.apm.apm.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    public fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://app.ticketmaster.com/discovery/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    public fun getRetrofitBandsInTown(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rest.bandsintown.com/artists/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    public fun getRetrofitMusicGenres(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://app.ticketmaster.com/discovery/v2/classifications/segments/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}