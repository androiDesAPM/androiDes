package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProfile : GetNavigationBarActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myprofile)

        val user = Firebase.auth.currentUser
        val email = user?.email

        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.data["email"] == email) {
                    val name = document.data["fullName"].toString()
                    val profileFullName = findViewById<EditText>(R.id.profileFullName)
                    profileFullName.setText(name)
                }
            }
        }


        val ticketsButton = findViewById<Button>(R.id.ticketsButton)
        ticketsButton.setOnClickListener {
            val intent = Intent(this, MyTicketsActivity::class.java)
            startActivity(intent)
        }

        val walletButton = findViewById<Button>(R.id.walletButton)
        walletButton.setOnClickListener {
            val intent = Intent(this, MyCoinsActivity::class.java)
            startActivity(intent)
        }

        //Creamos la barra inferior
        this.getNavigationView()
    }
}