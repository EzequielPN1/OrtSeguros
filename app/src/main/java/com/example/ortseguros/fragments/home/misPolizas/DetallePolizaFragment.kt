package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ortseguros.R
import com.example.ortseguros.fragments.home.siniestros.DetalleSiniestroFragmentArgs

class DetallePolizaFragment : Fragment() {

    private lateinit var viewModelDetallePoliza: DetallePolizaViewModel
    private lateinit var v: View



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModelDetallePoliza = ViewModelProvider(this)[DetallePolizaViewModel::class.java]
        v = inflater.inflate(R.layout.fragment_detalle_poliza, container, false)





        return v
    }



    override fun onStart() {
        super.onStart()

        val poliza = DetallePolizaFragmentArgs.fromBundle(requireArguments()).Poliza

    }
}