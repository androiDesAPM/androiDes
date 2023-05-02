package com.apm.apm.objects

import android.graphics.Bitmap
import java.time.LocalDate

class Concert (val concertLocationName: String,
               val concertDate: LocalDate,
               val concertArtistName: String,
               val imageUrl: String? = null){
}