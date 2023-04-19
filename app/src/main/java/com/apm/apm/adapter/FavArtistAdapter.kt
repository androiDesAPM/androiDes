package com.apm.apm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.R

class FavArtistAdapter : RecyclerView.Adapter<FavArtistAdapter.TuViewHolder>() {

    private val items = listOf("Item 1", "Item 2", "Item 3")

    class TuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            val nameTextView: TextView = itemView.findViewById(R.id.nombreArtista)
            nameTextView.text = item
        }
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.concerts_fav_artist_row, parent, false)
        return TuViewHolder(view)
    }

    override fun onBindViewHolder(holder: TuViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
