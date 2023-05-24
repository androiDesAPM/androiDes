package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.TicketAdapter
import com.apm.apm.data.BuyTicketOption
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BuyTicketsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ticketAdapter: TicketAdapter
    private lateinit var job: Job
    val options = mutableListOf<BuyTicketOption>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buy_ticket_options, container, false)

        recyclerView = view.findViewById(R.id.fragment_buy_ticket)
        ticketAdapter = TicketAdapter(options)

        recyclerView.adapter = ticketAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar: ProgressBar = view.findViewById(R.id.progressBarFutureConcerts)
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            getTicketOptionsCorrutine(progressBar)
        }
    }

    private fun getTicketOptionsCorrutine(progressBar: ProgressBar) {
        job = lifecycleScope.launch {

            //Añadir los conciertos encontrados
            options.add(BuyTicketOption("Ticketmaster"))
            options.add(BuyTicketOption("Página oficial del artista"))
            options.add(BuyTicketOption("Entradas.com"))

            ticketAdapter.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE
        }

        progressBar.visibility = View.VISIBLE

        println("Cargando opciones de compra ....") // o thread principal continúa durante o delay da corutina
        //Thread.sleep(5000L) // bloquéase o thread actual durante dous segundos

    }


}
