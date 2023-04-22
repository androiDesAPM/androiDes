package com.apm.apm.api

import com.apm.apm.data.ConcertsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getFavArtistsConcerts(@Url url:String): Response<List<ConcertsResponse>>
}