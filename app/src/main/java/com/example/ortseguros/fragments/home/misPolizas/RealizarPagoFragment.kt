package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R

class RealizarPagoFragment : Fragment() {

    private lateinit var viewModelRealizarPago: RealizarPagoViewModel
    private lateinit var v: View
    private lateinit var btnRealizarPago : Button
    private lateinit var txtPrecioRealizarPago:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v =  inflater.inflate(R.layout.fragment_realizar_pago, container, false)
        viewModelRealizarPago = ViewModelProvider(this)[RealizarPagoViewModel::class.java]
        btnRealizarPago = v.findViewById(R.id.btnRealizarPago)
        txtPrecioRealizarPago = v.findViewById(R.id.txtPrecioRealizarPago)


        viewModelRealizarPago.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModelRealizarPago.setToastMessage("")

            }
        }


        return v
    }


    override fun onStart() {
        super.onStart()

        val pago = RealizarPagoFragmentArgs.fromBundle(requireArguments()).Pago
        val poliza = RealizarPagoFragmentArgs.fromBundle(requireArguments()).poliza
        txtPrecioRealizarPago.text = "El monto a abonar es de: ${pago.precio}"




        btnRealizarPago.setOnClickListener {
            viewModelRealizarPago.realizarPago(pago, poliza) { exito ->
                if (exito) {
                 findNavController().navigateUp()
                }
            }
        }



    }








}