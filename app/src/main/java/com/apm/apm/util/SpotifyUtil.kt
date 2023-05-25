package com.apm.apm.util

import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.data.SpotifyTokenResponse

class SpotifyUtil {

    suspend fun authorizeSpotify(): String {
        val apiService = ApiClient().getAuthorizeSpotifyAPI().create(APIService::class.java)

        val call = apiService.authorizeSpotify("token","client_credentials",
            "b9a35122785346ab8edb7fa0e41dfcb6", "d19a3035c853425c937a61d91bd2dde9")

        val token: SpotifyTokenResponse? = call.body()

        if (token != null) {
            return token.token_value
        }
        return ""
    }
}