package com.apm.apm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.R
import com.apm.apm.objects.Artist

class MyProfileFavArtistsAdapter (private val dataSet: List<String>) : RecyclerView.Adapter<MyProfileFavArtistsAdapter.MyProfileFavArtistsViewHolder>() {

    class MyProfileFavArtistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            val artistName: TextView = itemView.findViewById(R.id.myProfileArtistFavName)
            artistName.text = item
        }
    }

    override fun getItemCount() = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfileFavArtistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.myprofile_fav_artist_element, parent, false)
        return MyProfileFavArtistsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyProfileFavArtistsViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bind(dataSet[position])

    }

}