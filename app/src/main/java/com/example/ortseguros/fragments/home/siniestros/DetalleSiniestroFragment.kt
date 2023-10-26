package com.example.ortseguros.fragments.home.siniestros

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ortseguros.R
import com.example.ortseguros.entities.Siniestro


class DetalleSiniestroFragment : Fragment() {

    private lateinit var viewModelDetalleSiniestro: DetalleSiniestroViewModel
    private lateinit var v : View
    private lateinit var txtTituloMensaje: TextView
    private lateinit var txtMensajeDetalleSiniestro: TextView
    private lateinit var txtNomEmpladoEmpresa: TextView
    private lateinit var imagePerfil: ImageView
    private lateinit var txtAnterior:TextView
    private lateinit var txtPosterior:TextView

    private var mensajeActualIndex = 0

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
        txtAnterior = v.findViewById(R.id.txtAnterior)
        txtPosterior = v.findViewById(R.id.txtPosterior)

        return v
    }


    override fun onStart() {
        super.onStart()

        val siniestro = DetalleSiniestroFragmentArgs.fromBundle(requireArguments()).siniestro
        mensajeActualIndex = siniestro.mensajes.size - 1



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





            txtAnterior.setOnClickListener {
                if (mensajeActualIndex > 0) {
                    mensajeActualIndex--
                    mostrarMensaje(siniestro, mensajeActualIndex)

                } else {
                    Toast.makeText(requireContext(), "No hay mensajes anteriores", Toast.LENGTH_SHORT).show()
                }
            }

            txtPosterior.setOnClickListener {
                if (mensajeActualIndex < siniestro.mensajes.size - 1) {
                    mensajeActualIndex++
                    mostrarMensaje(siniestro, mensajeActualIndex)

                } else {
                    Toast.makeText(requireContext(), "No hay mensajes posteriores", Toast.LENGTH_SHORT).show()
                }
            }



    }


    private fun mostrarMensaje(siniestro: Siniestro, index: Int) {
        if (index >= 0 && index < siniestro.mensajes.size) {
            val mensajeActual = siniestro.mensajes[index]

            txtNomEmpladoEmpresa.text = mensajeActual.usuarioEmpresa
            txtMensajeDetalleSiniestro.text = mensajeActual.notificacion

            if (mensajeActual.imagenURL.isNotEmpty()) {
                Glide.with(this)
                    .load(mensajeActual.imagenURL)
                    .into(imagePerfil)
            }


        }

    }




}