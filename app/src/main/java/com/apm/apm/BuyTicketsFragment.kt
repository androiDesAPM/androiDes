package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.TicketAdapter
import com.apm.apm.data.BuyTicketOption

class BuyTicketsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ticketAdapter: TicketAdapter
    val options = listOf(
        BuyTicketOption("Ticketmaster"),
        BuyTicketOption("Página oficial del artista"),
        BuyTicketOption("Entradas.com")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buy_ticket_options, container, false)
        // Configuramos el RecyclerView
        val adapter = TicketAdapter(options)
        view.findViewById<RecyclerView>(R.id.fragment_buy_ticket).adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.fragment_buy_ticket)
        ticketAdapter = TicketAdapter(options)

        recyclerView.adapter = ticketAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }


}
