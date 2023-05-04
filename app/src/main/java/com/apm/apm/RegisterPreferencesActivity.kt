package com.apm.apm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegisterPreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_preferences)
        setup()
    }

    private fun setup(){
        val finishSetup = findViewById<Button>(R.id.finishSetup)
        val email = intent.getStringExtra("email")
        val provider = intent.getStringExtra("provider")
        showHome(email ?: "", ProviderType.valueOf(provider ?: ""))
//        finishSetup.setOnClickListener {
//        }
    }

    private fun showHome(email: String, provider: ProviderType){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }
}