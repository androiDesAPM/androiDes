package com.apm.apm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.R
import com.apm.apm.data.BuyTicketOption

class TicketAdapter (private val dataSet: List<BuyTicketOption>) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: BuyTicketOption) {
            val buyTicketOption: TextView = itemView.findViewById(R.id.buy_option_name)
            buyTicketOption.text = item.name
        }
    }

    override fun getItemCount() = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketAdapter.TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.buy_ticket_row, parent, false)
        return TicketAdapter.TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketAdapter.TicketViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.bind(dataSet[position])
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Aún no se ha creado el chat del concierto", Toast.LENGTH_SHORT).show()
        }
    }
}