package com.apm.apm

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
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
            if (fullName.text.isNotEmpty() && email.text.isNotEmpty() && password.text.isNotEmpty() && passwordRep.text.isNotEmpty()){
                if (password.text.toString() == passwordRep.text.toString()){
                    registerUser(email.text.toString(), password.text.toString(), fullName.text.toString())
                }
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
        val db = Firebase.firestore
        val user = hashMapOf(
            "email" to email,
            "fullName" to fullName
        )
        db.collection("users").add(user)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
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
