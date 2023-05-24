package com.apm.apm

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {
    var latLng: com.google.android.gms.maps.model.LatLng? = null
    var city: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val apiKey = BuildConfig.PLACES_API_KEY

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        autocompleteFragment?.setHint("City")

        // Specify the types of place data to return.
        autocompleteFragment!!.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                latLng = place.latLng
                city = place.name
            }

            override fun onError(status: Status) {
                Log.i("place", "An error occurred: $status")
            }
        })

        setup()
    }

    private fun setup(){
        val registerButton = findViewById<Button>(R.id.registerRegister)
        val fullName = findViewById<EditText>(R.id.registerFullName)
        val email = findViewById<EditText>(R.id.registerEmail)
        val password = findViewById<EditText>(R.id.registerPass)
        val passwordRep = findViewById<EditText>(R.id.registerRepeatPass)
        val loginHere = findViewById<ConstraintLayout>(R.id.registerLoginHere)

        registerButton.setOnClickListener {
            if (fullName.text.isNotEmpty() && email.text.isNotEmpty() && password.text.isNotEmpty() && passwordRep.text.isNotEmpty() && city.isNotEmpty()){
                if (password.text.toString() == passwordRep.text.toString()){
                    registerUser(email.text.toString(), password.text.toString(), fullName.text.toString())
                }
            } else {
                Toast.makeText(
                    this,
                    "Please fill all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginHere.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, password: String, fullName: String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                uploadBD(email, fullName)
                showSetup(it.result?.user?.email ?: "", ProviderType.BASIC)
            }else{
                showAlert()
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error registrando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun uploadBD(email: String, fullName: String){
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        val db = Firebase.firestore
        val username = "User" + uid?.substring(0, 3)
        val coins = 1000
        val latLonDB = hashMapOf(
            "lat" to latLng?.latitude,
            "lon" to latLng?.longitude,
            "city" to city
        )
        val userToUpload = hashMapOf(
            "username" to username,
            "email" to email,
            "fullName" to fullName,
            "coins" to coins,
            "location" to latLonDB,
        )
        db.collection("users").document(uid ?: "").set(userToUpload)
            .addOnSuccessListener { documentReference ->
                println("Good")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }

    private fun showSetup(email: String, provider: ProviderType){
        val intent = Intent(this, RegisterPreferencesActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }
}
