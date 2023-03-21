package com.apm.apm

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

internal class SearchArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_artist)

        val listView: ListView = findViewById(R.id.listViewArtists)
        val items = listOf("Cantante 1", "Cantante 2", "Cantante 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@SearchArtistActivity, ArtistDetailsActivity::class.java)
            intent.putExtra("selected_item", items[position])
            startActivity(intent)
        }
    }
}