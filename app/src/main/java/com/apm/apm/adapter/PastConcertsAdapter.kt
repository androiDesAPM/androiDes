package com.apm.apm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.R
import com.apm.apm.objects.Concert

class PastConcertsAdapter (private val dataSet: List<Concert>) : RecyclerView.Adapter<PastConcertsAdapter.PastConcertsViewHolder>() {

    class PastConcertsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Concert) {
            val concertName: TextView = itemView.findViewById(R.id.concertName1)
            concertName.text = item.concertArtistName
            val pastConcertDate: TextView = itemView.findViewById(R.id.concertDate1)
            pastConcertDate.text = item.concertDate.toString()
            val concertCountry1: TextView = itemView.findViewById(R.id.concertCountry1)
            concertCountry1.text = item.concertLocationName
        }
    }

    override fun getItemCount() = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastConcertsAdapter.PastConcertsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.artist_past_concerts_row, parent, false)
        return PastConcertsAdapter.PastConcertsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PastConcertsAdapter.PastConcertsViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bind(dataSet[position])

    }
}