package com.apm.apm
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class FutureConcertsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_future_concerts_list, container, false);
        val concertButton = viewFragment.findViewById<TextView>(R.id.VerMas1)
        concertButton.setOnClickListener {
            val intent = Intent(requireContext(), ConcertDetailsActivity::class.java)
            startActivity(intent)
        }
        val concertButton2 = viewFragment.findViewById<TextView>(R.id.VerMas2)
        concertButton2.setOnClickListener {
            val intent = Intent(requireContext(), ConcertDetailsActivity::class.java)
            startActivity(intent)
        }
        val chatButton1 = viewFragment.findViewById<ImageButton>(R.id.chatButton1)
        chatButton1.setOnClickListener {
            Toast.makeText(requireContext(), "Aún no se ha creado el chat del concierto", Toast.LENGTH_SHORT).show()
        }
        val chatButton2 = viewFragment.findViewById<ImageButton>(R.id.chatButton2)
        chatButton2.setOnClickListener {
            Toast.makeText(requireContext(), "Aún no se ha creado el chat del concierto", Toast.LENGTH_SHORT).show()
        }
        return viewFragment
    }
}
