package com.example.ortseguros.fragments.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ortseguros.R

class SiniestrosFragment : Fragment() {

    private lateinit var viewModel: SiniestrosViewModel
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_siniestros, container, false)
        viewModel = ViewModelProvider(this)[SiniestrosViewModel::class.java]

        return v
    }


}