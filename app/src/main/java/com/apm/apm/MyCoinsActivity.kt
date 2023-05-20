package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyCoinsActivity : AppCompatActivity() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mywallet)

        val user = Firebase.auth.currentUser
        val uid = user?.uid

        db.collection("users").document(uid ?: "").get().addOnSuccessListener { result ->
            val name = result?.data?.get("username").toString()
            val profileFullName = findViewById<TextView>(R.id.usernameProfile)
            profileFullName.setText(name)

            val email = result?.data?.get("email").toString()
            val emailProfile = findViewById<TextView>(R.id.emailProfile)
            emailProfile.text = email

            val coins = result?.data?.get("coins").toString()
            val actualCoins = findViewById<TextView>(R.id.actualCoins)
            actualCoins.text = coins
        }

        val ticketBox1 = findViewById<Button>(R.id.donarButton)
        ticketBox1.setOnClickListener {
            Toast.makeText(this, "Donación realizada con éxito", Toast.LENGTH_LONG).show()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.navigation_map -> {
                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                }
                R.id.navigation_user -> {
                    val intent = Intent(this, MyProfile::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        val donateButton = findViewById<Button>(R.id.donarButton)
        donateButton.setOnClickListener {
            donateCoins()
        }

    }

    fun donateCoins() {
        val user = Firebase.auth.currentUser
        val uid = user?.uid

        val coinsToDonate = findViewById<EditText>(R.id.coinsToDonate)
        val coinsToDonateValue = coinsToDonate.text.toString().toInt()

//        get actual coins and substract coins to donate. If actual coins results in negative show toast error
        db.collection("users").document(uid ?: "").get().addOnSuccessListener { result ->
            val coins = result?.data?.get("coins").toString().toInt()
            val actualCoins = coins - coinsToDonateValue
            if (actualCoins < 0) {
                Toast.makeText(this, "No tienes suficientes monedas", Toast.LENGTH_LONG).show()
            } else {
                db.collection("users").document(uid ?: "").update("coins", actualCoins)
                val artistToDonate = findViewById<EditText>(R.id.artistToDonate)
                val donation = hashMapOf(
                    "artist" to artistToDonate.text.toString(),
                    "donation" to coinsToDonateValue
                )
                db.collection("users").document(uid ?: "").update("donations", FieldValue.arrayUnion(donation))
                Toast.makeText(this, "Donación realizada con éxito", Toast.LENGTH_LONG).show()
                val actualCoinsTextView = findViewById<TextView>(R.id.actualCoins)
                actualCoinsTextView.text = actualCoins.toString()
            }
        }
    }

}