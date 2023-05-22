package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.ArtistPhotosAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ArtistPhotosActivity: GetNavigationBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_photos)
        val artistId = intent.getStringExtra("artistId")
        val db = Firebase.firestore

        val artistRef = artistId?.let { db.collection("artists").document(it) }

        artistRef?.get()?.addOnSuccessListener { document ->
            if (document.exists()) {
                val artistData = document.data
                val imageUrls = artistData?.get("images") as? List<String>

                if (imageUrls != null && imageUrls.isNotEmpty()) {
                    // La lista de imágenes no está vacía
                    // Configurar el RecyclerView con el ArtistPhotoAdapter y la lista de imágenes
                    val recyclerView = findViewById<RecyclerView>(R.id.artist_photos_row)
                    val adapter = ArtistPhotosAdapter(this, imageUrls)
                    recyclerView.adapter = adapter
                } else {
                    // La lista de imágenes está vacía
                    Toast.makeText(applicationContext, "No se han añadido imágenes todavía", Toast.LENGTH_SHORT).show()
                }
            } else {
                // El documento del artista no existe, se crea
                val artistData = HashMap<String, Any>() // Agrega los campos necesarios
                db.collection("artists").document(artistId)
                    .set(artistData)
                    .addOnSuccessListener {
                        // El documento del artista se creó exitosamente
                    }
                    .addOnFailureListener { exception ->
                        // Ocurrió un error al crear el documento del artista
                        println("Error al crear el documento del artista: $exception")
                    }
            }
        }?.addOnFailureListener { exception ->
            // Error al obtener los datos del artista desde Firestore
            Toast.makeText(applicationContext, "Error al obtener los datos del artista", Toast.LENGTH_SHORT).show()
        }

        //Creamos la barra inferior
        this.getNavigationView()

    }

}