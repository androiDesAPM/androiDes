package com.apm.apm.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    public fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rest.bandsintown.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}