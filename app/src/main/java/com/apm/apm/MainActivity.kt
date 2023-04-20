package com.apm.apm

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import com.apm.apm.objects.Concert
import kotlinx.coroutines.*
import java.time.LocalDate

class MainActivity : GetNavigationBarActivity() {

    private lateinit var searchView: SearchView
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var searchArtistFragment: SearchArtistFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set de la vista
        setContentView(R.layout.home_page)

//        getConcertsCorrutine2(findViewById(R.id.progressba2r))

        //Funcionalidad para el buscador del artista
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.searchArtistHomeView)
        fragmentContainer = findViewById(R.id.frameLayoutSearch)
        searchArtistFragment = SearchArtistFragment()

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            //TODO borrar esto si al final no metemos sugerencias
            override fun onQueryTextChange(newText: String): Boolean {
                val isQueryEmpty = newText.isNullOrEmpty()
                if (isQueryEmpty) {
                    supportFragmentManager.beginTransaction()
                        .remove(searchArtistFragment)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutSearch, searchArtistFragment)
                        .addToBackStack(null)
                        .commit()
                }
                return true
            }
        })

        val clearButton = findViewById<Button>(R.id.clear_button)
        clearButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
            searchView.setQuery("", false)
            supportFragmentManager.beginTransaction()
                .remove(searchArtistFragment)
                .commit()
        }

        //Creamos la barra inferior
        this.getNavigationView()
    }

//    private fun getConcertsCorrutine2(progressBar: ProgressBar) {
//        GlobalScope.launch { // créase unha nova corrutina en segundo plano
//            delay(3000L) // delay non bloqueante (do thread actual) de 1000 milisegundos
//
//            progressBar.visibility = View.INVISIBLE
//        }
//
//        progressBar.visibility = View.VISIBLE
//
//        println("Cargando conciertos ....") // o thread principal continúa durante o delay da corutina
//        Thread.sleep(5000L) // bloquéase o thread actual durante dous segundos
//    }
}