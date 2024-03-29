package com.apm.apm

import android.app.AlertDialog
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File

class AuthActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        auth = Firebase.auth
        setup()
        val cacheFileFavArtists = File(this.cacheDir, "fav_artists_concerts_cache")
        if (cacheFileFavArtists.exists()) {
            cacheFileFavArtists.writeText("")
        }
        val cacheFileNear = File(this.cacheDir, "near_concerts_cache")
        if (cacheFileNear.exists()) {
            cacheFileNear.writeText("")
        }
        val cacheFileFavGenres = File(this.cacheDir, "fav_genres_concerts_cache")
        if (cacheFileFavGenres.exists()) {
            cacheFileFavGenres.writeText("")
        }
    }


    private fun setup(){
        val registerButton = findViewById<Button>(R.id.register)
        val signInButton = findViewById<Button>(R.id.sign_in_button)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)


        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        signInButton.setOnClickListener {
            if (username.text.isNotEmpty() && password.text.isNotEmpty()){
                auth.signInWithEmailAndPassword(username.text.toString(),
                    password.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }


    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }

}