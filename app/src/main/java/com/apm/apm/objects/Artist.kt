package com.apm.apm.objects

class Artist (val artistId: String,   //Id de spotify
              val completeName: String,
              val imageUrl: String? = null,
              val genres: List<String>,
              val spotifyUri: String){
}