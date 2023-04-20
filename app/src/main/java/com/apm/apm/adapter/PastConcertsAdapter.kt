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

        //TODO mpombo: añadir toast
        /*
        val cameraButton = viewFragment.findViewById<ImageButton>(R.id.cameraIcon1)
        cameraButton.setOnClickListener {
            Toast.makeText(requireContext(), "Se ha añadido la foto al album del concierto", Toast.LENGTH_SHORT).show()
        }
        val photoButton = viewFragment.findViewById<ImageButton>(R.id.concertPhotos1)
        photoButton.setOnClickListener {
            Toast.makeText(requireContext(), "Aún no hay fotos en el album", Toast.LENGTH_SHORT).show()
        }
        val cameraButton2 = viewFragment.findViewById<ImageButton>(R.id.cameraIcon2)
        cameraButton2.setOnClickListener {
            Toast.makeText(requireContext(), "Se ha añadido la foto al album del concierto", Toast.LENGTH_SHORT).show()
        }
        val photoButton2 = viewFragment.findViewById<ImageButton>(R.id.concertPhotos2)
        photoButton2.setOnClickListener {
            Toast.makeText(requireContext(), "Aún no hay fotos en el album", Toast.LENGTH_SHORT).show()
        }
        val cameraButton3 = viewFragment.findViewById<ImageButton>(R.id.cameraIcon3)
        cameraButton3.setOnClickListener {
            Toast.makeText(requireContext(), "Se ha añadido la foto al album del concierto", Toast.LENGTH_SHORT).show()
        */
    }
}