package com.apm.apm.data

import com.google.gson.annotations.SerializedName

data class SpotifyTokenResponse(
    @SerializedName("access_token")
    val token_value: String
)