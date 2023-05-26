package com.apm.apm.objects

import android.os.Parcel
import android.os.Parcelable

class Concert(
    val ticketMasterEventId: String,
    val concertLocationName: String,
    val concertDate: String,
    val concertArtistName: String,
    val imageUrl: String? = null,
    val concertCity: String? = "Unknown",
    val concertState: String? = "Unknown",
    val concertAddress: String? = "Unknown",
    val concertLongitude: String? = null,
    val concertLatitude: String? = null,
    val price: String? = null,
    val currency: String? = null,
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString()?: "Unknown",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: null,
        parcel.readString() ?: "Unknown",
        parcel.readString() ?: "Unknown",
        parcel.readString() ?: "Unknown",
        parcel.readString() ?: null,
        parcel.readString() ?: null,
        parcel.readString() ?: null,
        parcel.readString() ?: null,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ticketMasterEventId)
        parcel.writeString(concertLocationName)
        parcel.writeString(concertDate)
        parcel.writeString(concertArtistName)
        parcel.writeString(imageUrl)
        parcel.writeString(concertCity)
        parcel.writeString(concertState)
        parcel.writeString(concertAddress)
        parcel.writeString(concertLongitude)
        parcel.writeString(concertLatitude)
        parcel.writeString(price)
        parcel.writeString(currency)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Concert> {
        override fun createFromParcel(parcel: Parcel): Concert {
            return Concert(parcel)
        }

        override fun newArray(size: Int): Array<Concert?> {
            return arrayOfNulls(size)
        }
    }

}