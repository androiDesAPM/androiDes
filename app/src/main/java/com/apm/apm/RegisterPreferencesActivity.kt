package com.apm.apm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterPreferencesActivity : AppCompatActivity() {

    lateinit var genres : HashMap<String,String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_preferences)
        setup()
    }

    private fun uploadDB(key: String, value: String){
        val db = Firebase.firestore
        val genre = hashMapOf(
            "key" to key,
            "value" to value
        )
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        db.collection("users").document(uid ?: "").update("genres", FieldValue.arrayUnion(genre))
    }

    private fun setup(){
        val genresListView = findViewById<ListView>(R.id.listgenres)
        val genresList = arrayListOf<String>()

        val finishSetup = findViewById<Button>(R.id.finishSetup)
        val dropDown = findViewById<Spinner>(R.id.genresDropDown)

        val email = intent.getStringExtra("email")
        val provider = intent.getStringExtra("provider")

        val db = Firebase.firestore

        db.collection("genres").get().addOnSuccessListener {
            for (document in it) {
                if (document.id == "genres") {
                    genres = document.data as HashMap<String,String>
                    val genresStringList = genres.keys.toList()
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genresStringList)
                    dropDown.adapter = adapter
                }
            }
        }

        val adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, genresList)
        genresListView.adapter = adapter

        val maxGenres = 3
        dropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (genresList.size < maxGenres) {
                    val genre = dropDown.selectedItem.toString()
                    if (genre != "Undefined"){
                        genresList.add(genre)
                        adapter.notifyDataSetChanged()

                        val value = genres[genre]
                        uploadDB(genre, value ?: "")

                    }
                }
            }
        }

        finishSetup.setOnClickListener {
            showHome(email ?: "", ProviderType.valueOf(provider ?: ""))
        }
    }

    private fun showHome(email: String, provider: ProviderType){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }
}