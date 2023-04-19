package com.apm.apm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment

class PastConcertsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_past_concerts_list, container, false);
        val cameraButton = viewFragment.findViewById<ImageButton>(R.id.cameraIcon1)
        cameraButton.setOnClickListener {
            Toast.makeText(requireContext(), "Se ha añadido la foto al album del concierto", Toast.LENGTH_SHORT).show()
        }
        val photoButton = viewFragment.findViewById<ImageButton>(R.id.concertPhotos1)
        photoButton.setOnClickListener {
            Toast.makeText(requireContext(), "Aún no hay fotos en el album", Toast.LENGTH_SHORT).show()
        }
        val cameraButton2 = viewFragment.findViewById<ImageButton>(R.id.cameraIcon2)
        cameraButton2.setOnClickListener {
            Toast.makeText(requireContext(), "Se ha añadido la foto al album del concierto", Toast.LENGTH_SHORT).show()
        }
        val photoButton2 = viewFragment.findViewById<ImageButton>(R.id.concertPhotos2)
        photoButton2.setOnClickListener {
            Toast.makeText(requireContext(), "Aún no hay fotos en el album", Toast.LENGTH_SHORT).show()
        }
        val cameraButton3 = viewFragment.findViewById<ImageButton>(R.id.cameraIcon3)
        cameraButton3.setOnClickListener {
            Toast.makeText(requireContext(), "Se ha añadido la foto al album del concierto", Toast.LENGTH_SHORT).show()
        }
        val photoButton3 = viewFragment.findViewById<ImageButton>(R.id.concertPhotos3)
        photoButton3.setOnClickListener {
            Toast.makeText(requireContext(), "Aún no hay fotos en el album", Toast.LENGTH_SHORT).show()
        }
        return viewFragment
    }
}