package com.example.ortseguros.fragments.home.cuenta

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ortseguros.R

class PreguntasFrecuentesFragment : Fragment() {


    private lateinit var viewModel: PreguntasFrecuentesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preguntas_frecuentes, container, false)
    }


}