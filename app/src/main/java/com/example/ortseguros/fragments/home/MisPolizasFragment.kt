package com.example.ortseguros.fragments.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.ortseguros.R

class MisPolizasFragment : Fragment() {



    private lateinit var viewModel: MisPolizasViewModel

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_mis_polizas, container, false)
        viewModel = ViewModelProvider(this)[MisPolizasViewModel::class.java]
        return v
    }



    override fun onStart() {
        super.onStart()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Aquí puedes realiza una accion
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}