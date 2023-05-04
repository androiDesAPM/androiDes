package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.apm.adapter.MyProfileFavArtistsAdapter
import com.apm.apm.objects.Artist
import kotlinx.coroutines.Job

class MyProfileFavArtistsFragment : Fragment(), LifecycleOwner {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyProfileFavArtistsAdapter
    private lateinit var job: Job
    private val artists = mutableListOf<Artist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewFragment =
            inflater.inflate(R.layout.myprofile_fav_artists, container, false);

        recyclerView = viewFragment.findViewById(R.id.myProfileFavArtistsRecyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        artists.add(0, Artist("a1", "BTS", null, "a"));

//        val storage = Firebase.storage
//        storage.toString()
//
//        val database = FirebaseFirestore.getInstance()
//        val documentRef = database.collection("collection").document("user")
//
//        documentRef.toString()

        adapter = MyProfileFavArtistsAdapter(artists)
        recyclerView.adapter = adapter

        return viewFragment
    }
}