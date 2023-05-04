package com.apm.apm

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout

import com.google.firebase.auth.FirebaseAuth


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
                    registerUser(email.text.toString(), password.text.toString())
                }
            }
        }

        loginHere.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, password: String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
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

    private fun showHome(email: String, provider: ProviderType){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }
}
