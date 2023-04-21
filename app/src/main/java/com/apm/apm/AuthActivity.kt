package com.apm.apm

import android.app.AlertDialog
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        setup()
    }


    private fun setup(){
//        title = "Autenticación"
        val registerButton = findViewById<Button>(R.id.register)
        val signInButton = findViewById<Button>(R.id.sign_in_button)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)


        registerButton.setOnClickListener {
            if (username.text.isNotEmpty() && password.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(username.text.toString(),
                    password.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }
        }

        signInButton.setOnClickListener {
            if (username.text.isNotEmpty() && password.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(username.text.toString(),
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