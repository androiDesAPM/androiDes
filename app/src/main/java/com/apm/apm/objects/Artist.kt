package com.apm.apm.objects

class Artist (val artistId: String,
              val completeName: String,
              val imageUrl: String? = null,
              val genres: List<String>,
              val spotifyUri: String){
}