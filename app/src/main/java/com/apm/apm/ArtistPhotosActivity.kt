package com.apm.apm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
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
    private var artistId = 0
    val imagesList: MutableList<Bitmap> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_photos)
        //val artistId = intent.getStringExtra("artistId")
        this.artistId = 123
        val db = Firebase.firestore
        val recyclerView = findViewById<RecyclerView>(R.id.artist_photos_row)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = ArtistPhotosAdapter(this, emptyList())
        recyclerView.adapter = adapter

        //Funcionalidad para subir una imagen
        val uploadPhotoButton = findViewById<ImageButton>(R.id.uploadPhotoButton)
        uploadPhotoButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = "image/*"
                imagePickerActivityResult.launch(galleryIntent)
            }
        })
        val artistsRef2 = artistId?.let { storage.reference.child("artists/$artistId")}
        if (artistsRef2 != null) {
            val listAll = artistsRef2.listAll()

        }


        /****************************La edición buena*************************************/

        val storageRef1 = storage.reference
        val artistsRef1 = storageRef1.child("artists/123")

        artistsRef1.list(12 * 1)
            .addOnSuccessListener { listResult ->
                val items = listResult.items
                var stringList: ArrayList<String> = ArrayList()
                if (items.isNotEmpty()) { //ES ESTE ES ESTE
                    val imagePromises = items.take(12).map { item ->
//                        val downloadUrl = item.downloadUrl.toString()
                        val artistImageView = findViewById<ImageView>(R.id.imageView)
                        var download1 = item.downloadUrl.addOnSuccessListener{
                            Glide.with(this@ArtistPhotosActivity)
                                .load(it)
                                .into(artistImageView)

                            Log.e("Firebase", "download passed")
                        }
                        /**************************************Con este cacho funciona********************************************/
//                        val artistImageView = findViewById<ImageView>(R.id.imageView)
//                        var download1 = storage.reference.child("artists/123/a-brain-riding-a-rocketship.jpg").downloadUrl.addOnSuccessListener{
//                            Glide.with(this@ArtistPhotosActivity)
//                                .load(it)
//                                .into(artistImageView)
//
//                            Log.e("Firebase", "download passed")
//                        }
                        /********************************************************************************************************/

//                        val downloadUrl = item.downloadUrl.addOnSuccessListener{
//                            Glide.with(this@ArtistPhotosActivity)
//                                .load(it)
//                                .into(artistImageView)
//
//                            Log.e("Firebase", "download passed")
//                        }.addOnFailureListener {
//                            Log.e("Firebase", "Failed in downloading")
//                        }
//                        val downloadUrl = item.toString()
//                        stringList.add(downloadUrl)
//                        adapter.updateData(stringList)
//                        val imageRef = storageRef1.child(item.name)
//                        imageRef.getBytes(ONE_MEGABYTE.toLong()).asDeferred()
                    }
//                    CoroutineScope(Dispatchers.IO).launch {
////                        val bitmaps = imagePromises.awaitAll().mapNotNull { bytes ->
////                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                        }
////                        withContext(Dispatchers.Main) {
////                            imagesList.addAll(bitmaps)
////                            recyclerView.adapter?.notifyDataSetChanged()
//                        }
//                    }
                }
            }

        /****************************La manera que parece que funciona*************************************/

//        val storageRef = storage.reference
//        val artistsRef = storageRef.child("artists/123")
//
//        artistsRef.list(12 * 1)
//            .addOnSuccessListener { listResult ->
//                val items = listResult.items
//                if (items.isNotEmpty()) { //ES ESTE ES ESTE
//                    val imagePromises = items.take(12).map { item ->
//                        val imageRef = artistsRef.child(item.name)
//                        imageRef.getBytes(ONE_MEGABYTE.toLong()).asDeferred()
//                    }
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val bitmaps = imagePromises.awaitAll().mapNotNull { bytes ->
//                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                        }
//                        withContext(Dispatchers.Main) {
//                            imagesList.addAll(bitmaps)
//                            recyclerView.adapter?.notifyDataSetChanged()
//                        }
//                    }
//                }
//            }


        /****************************Otra manera*************************************/

//        val artistRef = artistId?.let { storage.reference.child("artists/$artistId") }
//
//        artistRef?.listAll()?.addOnSuccessListener { result ->
//            val imageUrls = result.items.map { it.downloadUrl.toString() }
//            val downloadUrl2 = result.items.get(0).downloadUrl
//            if (imageUrls.isNotEmpty()) {
//                // La lista de imágenes no está vacía
//                adapter.updateData(imageUrls)
//                // Configurar el RecyclerView con el ArtistPhotoAdapter y la lista de imágenes
//               // val recyclerView = findViewById<RecyclerView>(R.id.artist_photos_row)
//                //val adapter = ArtistPhotosAdapter(this, imageUrls)
//                //recyclerView.adapter = adapter
//            } else {
//                // La lista de imágenes está vacía
//                Toast.makeText(applicationContext, "No se han añadido imágenes todavía", Toast.LENGTH_SHORT).show()
//            }
////            result.items.get(0).listAll().result.items.get(0).downloadUrl //TODO borrar esta linea
//
//
//        }?.addOnFailureListener { exception ->
//            // Error al obtener los datos del artista desde Firestore
//            Toast.makeText(applicationContext, "Error al obtener los datos del artista", Toast.LENGTH_SHORT).show()
//        }

        /****************************Otra manera*************************************/

//        storage.reference.child("artists/$artistId" +"123").getDownloadUrl().addOnSuccessListener{ result ->
//            val imageUrls = result.toString()
//            val result2 = result//TODO borrar esto
//            if (imageUrls.isNotEmpty()) {
//                // La lista de imágenes no está vacía
////                adapter.updateData(imageUrls)
//                // Configurar el RecyclerView con el ArtistPhotoAdapter y la lista de imágenes
//                // val recyclerView = findViewById<RecyclerView>(R.id.artist_photos_row)
//                //val adapter = ArtistPhotosAdapter(this, imageUrls)
//                //recyclerView.adapter = adapter
//            } else {
//                // La lista de imágenes está vacía
//                Toast.makeText(applicationContext, "No se han añadido imágenes todavía", Toast.LENGTH_SHORT).show()
//            }
//
//        }?.addOnFailureListener { exception ->
//            // Error al obtener los datos del artista desde Firestore
//            Toast.makeText(applicationContext, "Error al obtener los datos del artista", Toast.LENGTH_SHORT).show()
//        }

        this.getNavigationView()

    }




    /****************************Subida de la imagen*************************************/





    //Con esta funcion subimos la imagen
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
                val uploadTask = storage.reference.child("artists/$artistId/$sd").putFile(imageUri)

                // On success, download the file URL and display it
                uploadTask.addOnSuccessListener {
                    // using glide library to display the image
                    storage.reference.child("artists/$artistId/$sd").downloadUrl.addOnSuccessListener {
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
        private const val ONE_MEGABYTE = 1_048_576 // 1 megabyte en bytes

    }


}