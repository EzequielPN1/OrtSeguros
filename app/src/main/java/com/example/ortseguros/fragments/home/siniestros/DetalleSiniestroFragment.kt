package com.example.ortseguros.fragments.home.siniestros

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ortseguros.R

class DetalleSiniestroFragment : Fragment() {

    private lateinit var viewModel: DetalleSiniestroViewModel
    private lateinit var v : View
    private lateinit var fecha : TextView
    private lateinit var hora : TextView
    private lateinit var ubicacion : TextView
    private lateinit var descripcion : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_detalle_siniestro, container, false)
        viewModel = ViewModelProvider(this)[DetalleSiniestroViewModel::class.java]
        fecha = v.findViewById(R.id.txtFechaSiniestro)
        hora = v.findViewById(R.id.txtHoraSiniestro)
        ubicacion = v.findViewById(R.id.txtUbicacionSiniestro)
        descripcion = v.findViewById(R.id.txtDescripcionSiniestro)
        return v
    }


    override fun onStart() {
        super.onStart()

        val siniestro = DetalleSiniestroFragmentArgs.fromBundle(requireArguments()).siniestro
        fecha.text = siniestro.fecha
        hora.text = siniestro.hora
        ubicacion.text = siniestro.ubicacion
        descripcion.text = siniestro.descripcion

    }



}