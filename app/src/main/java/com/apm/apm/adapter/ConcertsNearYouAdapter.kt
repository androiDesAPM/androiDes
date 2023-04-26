package com.apm.apm.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.ConcertDetailsActivity
import com.apm.apm.R
import com.apm.apm.objects.Concert

class ConcertsNearYouAdapter(private val dataSet: List<Concert>) : RecyclerView.Adapter<ConcertsNearYouAdapter.ConcertNearYouViewHolder>() {

    class ConcertNearYouViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Concert) {
            val nameTextView: TextView = itemView.findViewById(R.id.nombreArtista)
            nameTextView.text = item.concertArtistName
            val generoArtista: TextView = itemView.findViewById(R.id.generoArtista)
            generoArtista.text = item.concertLocationName
            val fechaConcierto: TextView = itemView.findViewById(R.id.fechaConcierto)
            fechaConcierto.text = item.concertDate.toString()
        }
    }
    override fun getItemCount() = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcertNearYouViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.concerts_fav_artist_row, parent, false)
        return ConcertNearYouViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConcertNearYouViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bind(dataSet[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ConcertDetailsActivity::class.java)
            intent.putExtra("artistName", dataSet[position].concertArtistName) // Si deseas pasar algún dato a la actividad, puedes hacerlo aquí
            context.startActivity(intent)
        }
    }
}
