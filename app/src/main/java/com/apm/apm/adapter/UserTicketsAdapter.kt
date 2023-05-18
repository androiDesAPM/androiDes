package com.apm.apm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.R
import com.apm.apm.data.Ticket

class UserTicketsAdapter(private val tickets: List<Ticket>) :
    RecyclerView.Adapter<UserTicketsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tickets_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val artistNameTextView: TextView = itemView.findViewById(R.id.artistNameTicket)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTicket)
        private val dateTextView: TextView = itemView.findViewById(R.id.fechaTicket)


        fun bind(ticket: Ticket) {
            artistNameTextView.text = ticket.artistName
            locationTextView.text = ticket.location
            dateTextView.text = ticket.date
        }
    }
}

