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
    private lateinit var imageDanioTotal : ImageView
    private lateinit var imageRespCivil : ImageView
    private lateinit var imageGranizo : ImageView
    private lateinit var imageRoboParcial : ImageView
    private lateinit var imageRoboTotal : ImageView
    private lateinit var inputSumaAsegurada: TextView
    private lateinit var inputValorCuota: TextView
    private lateinit var btnPagos: Button
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
        imageDanioTotal =v.findViewById(R.id.imageDanioTotal)
        imageRespCivil = v.findViewById(R.id.imageRespCivil)
        imageGranizo=v.findViewById(R.id.imageGranizo)
        inputSumaAsegurada=v.findViewById(R.id.inputSumaAsegurada)
        inputValorCuota=v.findViewById(R.id.inputValorCuota)
        btnPagos =v.findViewById(R.id.btnPagos)
        imageRoboParcial = v.findViewById(R.id.imageRoboParcial)
        imageRoboTotal =v.findViewById(R.id.imageRoboTotal)
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

        inputMarcaModelo.text = "Marca: ${poliza.marcaModelo}"
        inputPatentePoliza.text = "Patente: ${poliza.patente}"
        inputFechaInicio.text = "Inicio de la poliza: ${poliza.fechaInicioPoliza}"

        val imageViews = mapOf(
            "danioTotal" to imageDanioTotal,
            "respCivil" to imageRespCivil,
            "granizo" to imageGranizo,
            "roboParcial" to imageRoboParcial,
            "roboTotal" to imageRoboTotal
        )

        viewModelDetallePoliza.determinarImagenesDeCobertura(poliza, imageViews)


        inputSumaAsegurada.text = "Suma asegurada: ${poliza.sumaAsegurada} pesos"
        inputValorCuota.text = "Valor de cuota: ${poliza.valorCuota} pesos"


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
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }



    }
}