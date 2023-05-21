package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.MyProfileFavArtistsAdapter
import com.apm.apm.api.APIService
import com.apm.apm.api.ApiClient
import com.apm.apm.data.ConcertsResponse
import com.apm.apm.mappers.ConcertMapper
import com.apm.apm.objects.Artist
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyProfileFavArtistsFragment : Fragment(), LifecycleOwner {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyProfileFavArtistsAdapter
    private lateinit var job: Job
    private val artists = mutableListOf<String>()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewFragment =
            inflater.inflate(R.layout.myprofile_fav_artists, container, false);

        recyclerView = viewFragment.findViewById(R.id.myProfileFavArtistsRecyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = MyProfileFavArtistsAdapter(artists)
        recyclerView.adapter = adapter

        return viewFragment

//        artists.add(0, Artist("a1", "BTS", null, "a"));

//        val storage = Firebase.storage
//        storage.toString()
//
//        val database = FirebaseFirestore.getInstance()
//        val documentRef = database.collection("collection").document("user")
//
//        documentRef.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            getFavArtists()
        }
    }

    private fun getFavArtists() {
        job = lifecycleScope.launch {

            val user = Firebase.auth.currentUser
            val uid = user?.uid

            db.collection("users").document(uid ?: "")
                .get().addOnSuccessListener { document ->
                if (document != null && document?.data?.get("artists") != null) {
                    val artistsFromDB = (document?.data?.get("artists") as ArrayList<String>).toList()
                    artists.addAll(artistsFromDB)

                    adapter.notifyDataSetChanged()
                }
            }

        }

        println("Cargando conciertos ....")
    }
}