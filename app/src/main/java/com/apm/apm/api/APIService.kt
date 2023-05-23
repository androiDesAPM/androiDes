package com.apm.apm.api

import com.apm.apm.data.ArtistSpotifyResponse
import com.apm.apm.data.BandsInTownResponse
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.data.MusicResponse
import com.apm.apm.data.SpotifyTokenResponse
import okhttp3.FormBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getFavArtistsConcerts(@Url url:String): Response<ConcertsResponse>
    @GET
    suspend fun getArtistPastConcerts(@Url url:String): Response<List<BandsInTownResponse>>
    @GET
    suspend fun getConcertsByGeoPoint(@Url url:String): Response<ConcertsResponse>

    @GET
    suspend fun getMusicGenres(@Url url:String): Response<MusicResponse>

    @FormUrlEncoded
    @POST
    suspend fun authorizeSpotify(@Url url:String, @Field("grant_type") grant_type: String,
                                 @Field("client_id") client_id: String,
                                 @Field("client_secret") client_secret: String): Response<SpotifyTokenResponse>

    @GET
    suspend fun getSpotifyArtistByName(@Url url:String, @Header("Authorization") access_token:String): Response<ArtistSpotifyResponse>
}