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

    private lateinit var viewModelDetalleSiniestro: DetalleSiniestroViewModel
    private lateinit var v : View
    private lateinit var txtTituloMensaje: TextView
    private lateinit var txtMensajeDetalleSiniestro: TextView
    private lateinit var txtNomEmpladoEmpresa: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_detalle_siniestro, container, false)
        viewModelDetalleSiniestro = ViewModelProvider(this)[DetalleSiniestroViewModel::class.java]
        txtTituloMensaje = v.findViewById(R.id.txtTituloMensaje)
        txtMensajeDetalleSiniestro = v.findViewById(R.id.txtMensajeDetalleSiniestro)
        txtNomEmpladoEmpresa = v.findViewById(R.id.txtNomEmpladoEmpresa)
        return v
    }


    override fun onStart() {
        super.onStart()

        val siniestro = DetalleSiniestroFragmentArgs.fromBundle(requireArguments()).siniestro

        viewModelDetalleSiniestro.mostrarTituloMesnaje() { mensajeEncontrado, mensaje ->
            if (mensajeEncontrado) {
                txtTituloMensaje.text = mensaje
            }
        }

        viewModelDetalleSiniestro.mostrarMensaje(siniestro) { mensajeEncontrado,mensaje,nombreEmpleado ->
            if (mensajeEncontrado) {
                txtMensajeDetalleSiniestro.text = mensaje
                txtNomEmpladoEmpresa.text = nombreEmpleado
            }
        }




    }



}