package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ortseguros.R

class NuevaPolizaFragment : Fragment() {


    private lateinit var viewModelNuevaPoliza: NuevaPolizaViewModel
    lateinit var v: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_nueva_poliza, container, false)
        viewModelNuevaPoliza = ViewModelProvider(this)[NuevaPolizaViewModel::class.java]








        return v
    }



}