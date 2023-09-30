package com.example.ortseguros.fragments.home.siniestros

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ortseguros.R

class NuevoSiniestroFragment : Fragment() {


    private lateinit var viewModel: NuevoSiniestroViewModel
    lateinit var v: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_nuevo_siniestro, container, false)
        viewModel = ViewModelProvider(this)[NuevoSiniestroViewModel::class.java]


        return v
    }





}