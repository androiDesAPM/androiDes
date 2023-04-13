package com.apm.apm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.data.BuyTicketOption
import com.apm.apm.R


class TicketAdapter(private val options: List<BuyTicketOption>) :
    RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Aquí puedes hacer referencia a los elementos de la vista
        val nameTextView: TextView = itemView.findViewById(R.id.buy_option_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        // Aquí inflamos la vista de la opción de compra de ticket y creamos el ViewHolder
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.buy_ticket_row, parent, false)
        return TicketViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        // Aquí actualizamos la vista con los datos de la opción de compra de ticket correspondiente
        val option = options[position]
        holder.nameTextView.text = option.name
    }

    override fun getItemCount(): Int {
        return options.size
    }
}