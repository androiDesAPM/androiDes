package com.apm.apm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.R
import com.squareup.picasso.Picasso

class ArtistPhotosAdapter(private val context: Context, private val photoList: List<String>) : RecyclerView.Adapter<ArtistPhotosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.artist_photos_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoUrl = photoList[position]
        Picasso.get()
            .load(photoUrl)
            .fit()
            .centerCrop()
            .into(holder.photoImageView)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.artistPhoto)
    }
}