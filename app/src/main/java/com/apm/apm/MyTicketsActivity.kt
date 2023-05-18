package com.apm.apm

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.UserTicketsAdapter
import com.apm.apm.data.Ticket
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyTicketsActivity : GetNavigationBarActivity() {

    private lateinit var ticketsAdapter: UserTicketsAdapter
    private lateinit var ticketsRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_tickets)

        ticketsRecyclerView = findViewById(R.id.user_tickets_recyclerView)
        ticketsRecyclerView.layoutManager = LinearLayoutManager(this)

        val user = Firebase.auth.currentUser
        val email = user?.email
        val db = Firebase.firestore

        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.data["email"] == email) {
                    val tickets = document.data["tickets"] as? ArrayList<HashMap<String, String>>
                    if (tickets != null) {
                        ticketsAdapter = UserTicketsAdapter(tickets.map {
                            Ticket(
                                it["concertArtistName"]!!,
                                it["concertLocationName"]!!,
                                it["concertDate"]!!
                            )
                        })

                        ticketsRecyclerView.adapter = ticketsAdapter
                    } else {
                        Toast.makeText(
                            this,
                            "Aún no has comprado ninguna entrada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        /*val three_points_button_1 = findViewById<ImageButton>(R.id.three_points_1)
        three_points_button_1.setOnClickListener {
            Toast.makeText(this, "No hay opciones disponibles", Toast.LENGTH_SHORT).show()
        }*/

        //Creamos la barra inferior
        this.getNavigationView()

    }
}
