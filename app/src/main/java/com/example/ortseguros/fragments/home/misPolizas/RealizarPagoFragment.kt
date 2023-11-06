package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.entities.Usuario

class RealizarPagoFragment : Fragment() {

    private lateinit var viewModelRealizarPago: RealizarPagoViewModel
    private lateinit var v: View
    private lateinit var btnRealizarPago : Button
    private lateinit var txtPrecioRealizarPago:TextView
    private lateinit var inputNumeroDeTarjeta: EditText
    private lateinit var inputFechaDeCaducidad: EditText
    private lateinit var inputTitular: EditText
    private lateinit var inputDniRealizarPago: EditText
    private lateinit var inputCodigoDeSeguridad: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v =  inflater.inflate(R.layout.fragment_realizar_pago, container, false)
        viewModelRealizarPago = ViewModelProvider(this)[RealizarPagoViewModel::class.java]
        btnRealizarPago = v.findViewById(R.id.btnRealizarPago)
        txtPrecioRealizarPago = v.findViewById(R.id.txtPrecioRealizarPago)
        inputNumeroDeTarjeta = v.findViewById(R.id.inputNumeroDeTarjeta)
        inputFechaDeCaducidad = v.findViewById(R.id.inputFechaDeCaducidad)
        inputTitular = v.findViewById(R.id.inputTitular)
        inputDniRealizarPago = v.findViewById(R.id.inputDniRealizarPago)
        inputCodigoDeSeguridad = v.findViewById(R.id.inputCodigoDeSeguridad)

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
        txtPrecioRealizarPago.text = v.context.getString(R.string.monto_abonar_text, pago.precio)


        viewModelRealizarPago.obtenerTarjetaDeCreditoUsuario { tarjetaDeCredito ->
            if (tarjetaDeCredito != null) {

                inputNumeroDeTarjeta.setText(tarjetaDeCredito.numeroDeTarjeta)
                inputFechaDeCaducidad.setText(tarjetaDeCredito.fechaCaducidad)
                inputTitular.setText(tarjetaDeCredito.titular)
                inputDniRealizarPago.setText(tarjetaDeCredito.dni)
                inputCodigoDeSeguridad.setText(tarjetaDeCredito.codigoSeguridad)
            }
        }

        btnRealizarPago.setOnClickListener {
            val numeroDeTarjetaTexto = inputNumeroDeTarjeta.text.toString()
            val fechaDeCaducidadTexto = inputFechaDeCaducidad.text.toString()
            val titularTexto = inputTitular.text.toString()
            val dniRealizarPagoTexto = inputDniRealizarPago.text.toString()
            val codigoDeSeguridadTexto = inputCodigoDeSeguridad.text.toString()

            if (viewModelRealizarPago.validarCampos(
                    numeroDeTarjetaTexto,
                    fechaDeCaducidadTexto,
                    titularTexto,
                    dniRealizarPagoTexto,
                    codigoDeSeguridadTexto
                )
            ) {

                val tarjetaDeCredito = Usuario.TarjetaDeCredito(
                    numeroDeTarjetaTexto,
                    fechaDeCaducidadTexto,
                    titularTexto,
                    dniRealizarPagoTexto,
                    codigoDeSeguridadTexto
                )


                viewModelRealizarPago.guardarTarjetaDeCreditoUsuario(tarjetaDeCredito) { exito ->
                    if (exito) {
                        viewModelRealizarPago.realizarPago(pago, poliza) { exitoPago ->
                            if (exitoPago) {
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }













}