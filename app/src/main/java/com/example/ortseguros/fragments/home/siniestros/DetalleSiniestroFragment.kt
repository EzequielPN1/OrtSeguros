package com.example.ortseguros.fragments.home.siniestros

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ortseguros.R

class DetalleSiniestroFragment : Fragment() {

    private lateinit var viewModelDetalleSiniestro: DetalleSiniestroViewModel
    private lateinit var v : View
    private lateinit var txtTituloMensaje: TextView
    private lateinit var txtMensajeDetalleSiniestro: TextView
    private lateinit var txtNomEmpladoEmpresa: TextView
    private lateinit var imagePerfil: ImageView
    private lateinit var txtVerMensajeAnterior:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_detalle_siniestro, container, false)
        viewModelDetalleSiniestro = ViewModelProvider(this)[DetalleSiniestroViewModel::class.java]
        txtTituloMensaje = v.findViewById(R.id.txtTituloMensaje)
        txtMensajeDetalleSiniestro = v.findViewById(R.id.txtMensajeDetalleSiniestro)
        txtNomEmpladoEmpresa = v.findViewById(R.id.txtNomEmpladoEmpresa)
        imagePerfil = v.findViewById(R.id.imagePerfil)

        return v
    }


    override fun onStart() {
        super.onStart()

        val siniestro = DetalleSiniestroFragmentArgs.fromBundle(requireArguments()).siniestro

        viewModelDetalleSiniestro.mostrarTituloMensaje() { mensajeEncontrado, mensaje ->
            if (mensajeEncontrado) {
                txtTituloMensaje.text = mensaje
            }
        }

        viewModelDetalleSiniestro.mostrarMensajeActual(siniestro) { mensajeEncontrado, mensaje ,nombreEmpleado, imagenURL ->
            if (mensajeEncontrado) {

                txtNomEmpladoEmpresa.text = nombreEmpleado
                txtMensajeDetalleSiniestro.text = mensaje

                if (imagenURL.isNotEmpty()) {
                    Glide.with(this)
                        .load(imagenURL)
                        .into(imagePerfil)
                }



            }
        }







    }



}