package com.apm.apm.api

import com.apm.apm.data.ArtistTicketMasterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ArtistService {
    @GET
    suspend fun getArtistDetails(@Url url:String): Response<ArtistTicketMasterResponse>
}