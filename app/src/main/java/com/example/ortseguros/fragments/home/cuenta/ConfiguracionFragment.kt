package com.example.ortseguros.fragments.home.cuenta

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ortseguros.R

class ConfiguracionFragment : Fragment() {


    private lateinit var viewModel: ConfiguracionViewModel
    lateinit var v: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_configuracion, container, false)
        return v

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfiguracionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}