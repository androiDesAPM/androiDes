package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.internal.TextWatcherAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProfile : GetNavigationBarActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myprofile)

        val myProfileFavGenresListView = findViewById<ListView>(R.id.myProfileFavGenresListView)
        val myProfileFavGenres = ArrayList<String>()
        val adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, myProfileFavGenres)
        myProfileFavGenresListView.adapter = adapter

        val user = Firebase.auth.currentUser
        val uid = user?.uid

        db.collection("users").document(uid ?: "").get().addOnSuccessListener { result ->
            val name = result?.data?.get("fullName").toString()
            val profileFullName = findViewById<EditText>(R.id.profileFullName)
            profileFullName.setText(name)

            val email = result?.data?.get("email").toString()
            val emailProfile = findViewById<TextView>(R.id.emailProfile)
            emailProfile.text = email

            val username = result?.data?.get("username").toString()
            val usernameProfile = findViewById<EditText>(R.id.usernameProfile)
            usernameProfile.setText(username)

            val genres = result?.data?.get("genres") as ArrayList<HashMap<String, String>>
            for (genre in genres) {
                myProfileFavGenres.add(genre["key"].toString())
            }
            adapter.notifyDataSetChanged()
        }

        val usernameProfile = findViewById<EditText>(R.id.usernameProfile)
        usernameProfile.addTextChangedListener(textWatcher)

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

        this.getNavigationView()
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (s.hashCode() == findViewById<EditText>(R.id.usernameProfile).text.hashCode()) {
                val username = findViewById<EditText>(R.id.usernameProfile).text.toString()
                val user = Firebase.auth.currentUser
                val uid = user?.uid
                db.collection("users").document(uid ?: "").update("username", username)
            }

        }
    }
}