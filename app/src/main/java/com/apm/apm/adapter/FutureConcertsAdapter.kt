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

class FutureConcertsAdapter (private val dataSet: List<Concert>) : RecyclerView.Adapter<FutureConcertsAdapter.FutureConcertsViewHolder>() {

    class FutureConcertsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Concert) {
            val concertName: TextView = itemView.findViewById(R.id.concertNameFuture)
            concertName.text = item.concertArtistName
            val futureConcertDate: TextView = itemView.findViewById(R.id.concertDateFuture)
            futureConcertDate.text = item.concertDate.toString()
            val concertCountry1: TextView = itemView.findViewById(R.id.concertCountryFuture)
            concertCountry1.text = item.concertLocationName
        }
    }

    override fun getItemCount() = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureConcertsAdapter.FutureConcertsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.artist_future_concerts_row, parent, false)
        return FutureConcertsAdapter.FutureConcertsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FutureConcertsAdapter.FutureConcertsViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bind(dataSet[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ConcertDetailsActivity::class.java)
            intent.putExtra("concert", dataSet[position]) // Si deseas pasar algún dato a la actividad, puedes hacerlo aquí
            context.startActivity(intent)
        }

        //TODO mpombo: añadir toast
//        val concertButton = viewFragment.findViewById<TextView>(R.id.VerMas1)
//        concertButton.setOnClickListener {
//            val intent = Intent(requireContext(), ConcertDetailsActivity::class.java)
//            startActivity(intent)
//        }
//        val concertButton2 = viewFragment.findViewById<TextView>(R.id.VerMas2)
//        concertButton2.setOnClickListener {
//            val intent = Intent(requireContext(), ConcertDetailsActivity::class.java)
//            startActivity(intent)
//        }
//        val chatButton1 = viewFragment.findViewById<ImageButton>(R.id.chatButton1)
//        chatButton1.setOnClickListener {
//            Toast.makeText(requireContext(), "Aún no se ha creado el chat del concierto", Toast.LENGTH_SHORT).show()
//        }
//        val chatButton2 = viewFragment.findViewById<ImageButton>(R.id.chatButton2)
//        chatButton2.setOnClickListener {
//            Toast.makeText(requireContext(), "Aún no se ha creado el chat del concierto", Toast.LENGTH_SHORT).show()
//        }
    }
}