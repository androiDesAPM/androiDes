package com.apm.apm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.ArtistPhotosAdapter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ArtistPhotosActivity: GetNavigationBarActivity() {
    private lateinit var adapter: ArtistPhotosAdapter
    private val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_photos)
        val artistId = intent.getStringExtra("artistId")
        val db = Firebase.firestore

        val artistRef = artistId?.let { db.collection("artists").document(it) }

        val recyclerView = findViewById<RecyclerView>(R.id.artist_photos_row)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = ArtistPhotosAdapter(this, emptyList())
        recyclerView.adapter = adapter

        //Funcionalidad para subir una imagen
        val uploadPhotoButton = findViewById<ImageButton>(R.id.uploadPhotoButton)
//        uploadPhotoButton.setOnClickListener {
//            // Aquí colocar el código que deseas ejecutar cuando se hace clic en el botón
//            // Por ejemplo, puedes iniciar la selección de la imagen aquí
//            val galleryIntent = Intent(Intent.ACTION_PICK)
//            galleryIntent.type = "image/*"
//            imagePickerActivityResult.launch(galleryIntent)
//        }

        uploadPhotoButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = "image/*"
                imagePickerActivityResult.launch(galleryIntent)
            }
        })


        artistRef?.get()?.addOnSuccessListener { document ->
            if (document.exists()) {
                val artistData = document.data
                val imageUrls = artistData?.get("images") as? List<String>

                if (imageUrls != null && imageUrls.isNotEmpty()) {
                    // La lista de imágenes no está vacía
                    adapter.updateData(imageUrls)
                    // Configurar el RecyclerView con el ArtistPhotoAdapter y la lista de imágenes
                   // val recyclerView = findViewById<RecyclerView>(R.id.artist_photos_row)
                    7//val adapter = ArtistPhotosAdapter(this, imageUrls)
                    //recyclerView.adapter = adapter
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



//         uploadPhotoButton.setOnClickListener {
//            // PICK INTENT picks item from data
//            // and returned selected item
//            val galleryIntent = Intent(Intent.ACTION_PICK)
//            // here item is type of image
//            galleryIntent.type = "image/*"
//            // ActivityResultLauncher callback
//            imagePickerActivityResult.launch(galleryIntent)
//        }

        //Creamos la barra inferior
        this.getNavigationView()

    }



    // Método para manejar el resultado de la selección de la imagen
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
//            val selectedImageUri = data?.data
//
//            if (selectedImageUri != null) {
//                // Aquí acciones con la imagen seleccionada, como subirla a Firebase Storage
//                // Obtener la ruta de la imagen usando selectedImageUri.path
//
//                Toast.makeText(applicationContext, "Ruta de la imagen: ${selectedImageUri.path}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {

                val artistImageView = findViewById<ImageView>(R.id.imageView)

                // getting URI of selected Image
                val imageUri: Uri? = result.data?.data

                // val fileName = imageUri?.pathSegments?.last()

                // extract the file name with extension
                val sd = getFileName(applicationContext, imageUri!!)

                // Upload Task with upload to directory 'file'
                // and name of the file remains same
                val uploadTask = storage.reference.child("file/$sd").putFile(imageUri)

                // On success, download the file URL and display it
                uploadTask.addOnSuccessListener {
                    // using glide library to display the image
                    storage.reference.child("upload/$sd").downloadUrl.addOnSuccessListener {
                        Glide.with(this@ArtistPhotosActivity)
                            .load(it)
                            .into(artistImageView)

                        Log.e("Firebase", "download passed")
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }


    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }


}