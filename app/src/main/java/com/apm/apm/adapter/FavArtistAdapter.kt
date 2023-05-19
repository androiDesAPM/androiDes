package com.apm.apm.adapter

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.ConcertDetailsActivity
import com.apm.apm.R
import com.apm.apm.objects.Concert
import com.squareup.picasso.Picasso

class FavArtistAdapter(private val dataSet: List<Concert>) : RecyclerView.Adapter<FavArtistAdapter.ConcertFavArtistViewHolder>() {

    class ConcertFavArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Concert) {
            val nameTextView: TextView = itemView.findViewById(R.id.nombreArtista)
            nameTextView.text = item.concertArtistName
            val generoArtista: TextView = itemView.findViewById(R.id.generoArtista)
            generoArtista.text = item.concertLocationName
            val fechaConcierto: TextView = itemView.findViewById(R.id.fechaConcierto)
            fechaConcierto.text = item.concertDate.toString()
            val imagenConcierto: ImageView = itemView.findViewById(R.id.imagenArtista)
            Picasso.get().load(item.imageUrl).into(imagenConcierto)
        }
    }
    override fun getItemCount() = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcertFavArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.concerts_fav_artist_row, parent, false)
        return ConcertFavArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConcertFavArtistViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bind(dataSet[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ConcertDetailsActivity::class.java)
            intent.putExtra("concert", dataSet[position]) // Si deseas pasar algún dato a la actividad, puedes hacerlo aquí
            context.startActivity(intent)
        }
    }
}
