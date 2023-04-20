package com.apm.apm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.R
import com.apm.apm.objects.Concert

class FavArtistAdapter(private val dataSet: List<Concert>) : RecyclerView.Adapter<FavArtistAdapter.ConcertFavArtistViewHolder>() {

    class ConcertFavArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Concert) {
            val nameTextView: TextView = itemView.findViewById(R.id.nombreArtista)
            nameTextView.text = item.concertArtistName
            val generoArtista: TextView = itemView.findViewById(R.id.generoArtista)
            generoArtista.text = item.concertLocationName

        }
    }
    override fun getItemCount() = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcertFavArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.concerts_fav_artist_row, parent, false)
        return ConcertFavArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConcertFavArtistViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }
}
