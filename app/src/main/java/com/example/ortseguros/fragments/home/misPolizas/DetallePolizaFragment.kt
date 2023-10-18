package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ortseguros.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class DetallePolizaFragment : Fragment() {

    private lateinit var viewModelDetallePoliza: DetallePolizaViewModel
    private lateinit var v: View

    private lateinit var inputMarcaModelo: TextView
    private lateinit var inputPatentePoliza: TextView
    private lateinit var inputFechaInicio: TextView
    private lateinit var inputDanioTotal: TextView
    private lateinit var inputRespCivil: TextView
    private lateinit var inputGranizo: TextView
    private lateinit var inputRoboParcial: TextView
    private lateinit var inputRoboTotal: TextView
    private lateinit var inputSumaAsegurada: TextView
    private lateinit var inputValorCuota: TextView
    private lateinit var btnPagos: Button

    private lateinit var imageFrente: ImageView
    private lateinit var imageLatIzq: ImageView
    private lateinit var imageLatDer: ImageView
    private lateinit var imagePosterior: ImageView

    private lateinit var btnBajaPoliza: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelDetallePoliza = ViewModelProvider(this)[DetallePolizaViewModel::class.java]
        v = inflater.inflate(R.layout.fragment_detalle_poliza, container, false)

        inputMarcaModelo=v.findViewById(R.id.inputMarcaModelo)
        inputPatentePoliza=v.findViewById(R.id.inputPatentePoliza)
        inputFechaInicio=v.findViewById(R.id.inputFechaInicio)
        inputDanioTotal=v.findViewById(R.id.inputDanioTotal)
        inputRespCivil=v.findViewById(R.id.inputRespCivil)
        inputGranizo=v.findViewById(R.id.inputGranizo)
        inputRoboParcial=v.findViewById(R.id.inputRoboParcial)
        inputRoboTotal=v.findViewById(R.id.inputRoboTotal)
        inputSumaAsegurada=v.findViewById(R.id.inputSumaAsegurada)
        inputValorCuota=v.findViewById(R.id.inputValorCuota)
        btnPagos =v.findViewById(R.id.btnPagos)

        imageFrente = v.findViewById(R.id.imageFrenteDetalle)
        imageLatIzq = v.findViewById(R.id.imageLatIzqDetalle)
        imageLatDer = v.findViewById(R.id.imageLatDerDetalle)
        imagePosterior = v.findViewById(R.id.imagePosteriorDetalle)

        btnBajaPoliza = v.findViewById(R.id.btnBajaPoliza)


        viewModelDetallePoliza.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModelDetallePoliza.setToastMessage("")
            }
        }

        return v
    }



    override fun onStart() {
        super.onStart()

        val poliza = DetallePolizaFragmentArgs.fromBundle(requireArguments()).Poliza

        inputMarcaModelo.text = poliza.marcaModelo
        inputPatentePoliza.text = poliza.patente
        inputFechaInicio.text = poliza.fechaInicioPoliza
        inputDanioTotal.text = if (poliza.danioTotal) "contratado" else "no"
        inputRespCivil.text = if (poliza.respCivil) "contratado" else "no"
        inputGranizo.text = if (poliza.granizo) "contratado" else "no"
        inputRoboParcial.text = if (poliza.roboParcial) "contratado" else "no"
        inputRoboTotal.text = if (poliza.roboTotal) "contratado" else "no"
        inputSumaAsegurada.text = "${poliza.sumaAsegurada} pesos"
        inputValorCuota.text = "${poliza.valorCuota} pesos"
        viewModelDetallePoliza.cargarImagenDesdeFirebase(poliza.uriImageFrente, imageFrente)
        viewModelDetallePoliza.cargarImagenDesdeFirebase(poliza.uriImageLatIzq, imageLatIzq)
        viewModelDetallePoliza.cargarImagenDesdeFirebase(poliza.uriImageLatDer, imageLatDer)
        viewModelDetallePoliza.cargarImagenDesdeFirebase(poliza.uriImagePosterior, imagePosterior)

        btnPagos.setOnClickListener{

            val action = DetallePolizaFragmentDirections.actionDetallePolizaFragmentToPagosFragment(poliza)
            findNavController().navigate(action)

        }

        btnBajaPoliza.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirmación")
            builder.setMessage("¿Está seguro de que desea dar de baja esta póliza?")

            builder.setPositiveButton("Sí") { dialog, _ ->
                // Si el usuario hace clic en "Sí", entonces se da de baja la póliza
                viewModelDetallePoliza.darBajaPoliza(poliza) { exito ->
                    if (exito) {
                        findNavController().navigateUp()
                    }
                }
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }



    }
}