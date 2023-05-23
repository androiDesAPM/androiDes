package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.ArtistPhotosAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

        //Funcionalidad para subir una imagen
        val uploadPhotoButton = findViewById<ImageButton>(R.id.uploadPhotoButton)
        uploadPhotoButton.setOnClickListener {
            // Crear un intent para seleccionar una imagen de la galería
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        //Creamos la barra inferior
        this.getNavigationView()

    }



    // Método para manejar el resultado de la selección de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data

            if (selectedImageUri != null) {
                // Aquí acciones con la imagen seleccionada, como subirla a Firebase Storage
                // Obtener la ruta de la imagen usando selectedImageUri.path

                Toast.makeText(applicationContext, "Ruta de la imagen: ${selectedImageUri.path}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }


}