package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import com.example.ortseguros.R



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

        viewModelDetallePoliza.cambiarEstado(poliza)

        inputMarcaModelo.text = v.context.getString(R.string.marca_text_detalle_poliza, poliza.marcaModelo)
        inputPatentePoliza.text = v.context.getString(R.string.patente_text_detalle_poliza, poliza.patente)
        inputFechaInicio.text = v.context.getString(R.string.inicio_poliza_text_detalle_poliza, poliza.fechaInicioPoliza)


        val imageViews = mapOf(
            v.context.getString(R.string.danio_total_mapeo_detalle_poliza) to imageDanioTotal,
            v.context.getString(R.string.resp_civil_mapeo_detalle_poliza) to imageRespCivil,
            v.context.getString(R.string.granizo_mapeo_detalle_poliza) to imageGranizo,
            v.context.getString(R.string.robo_parcial_mapeo_detalle_poliza) to imageRoboParcial,
            v.context.getString(R.string.robo_total_mapeo_detalle_poliza) to imageRoboTotal
        )


        viewModelDetallePoliza.determinarImagenesDeCobertura(poliza, imageViews)


        val sumaAsegurada = poliza.sumaAsegurada.toBigDecimal().toPlainString()
        inputSumaAsegurada.text = v.context.getString(R.string.suma_asegurada_text_detalle_poliza, sumaAsegurada)
        inputValorCuota.text = v.context.getString(R.string.valor_cuota_text_detalle_poliza, poliza.valorCuota)



        btnPagos.setOnClickListener{

            val action = DetallePolizaFragmentDirections.actionDetallePolizaFragmentToPagosFragment(poliza)
            findNavController().navigate(action)

        }

        btnBajaPoliza.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(v.context.getString(R.string.confirmacion_titulo_bajaPoliza))
            builder.setMessage(v.context.getString(R.string.confirmacion_mensaje_bajaPoliza))

            builder.setPositiveButton(v.context.getString(R.string.boton_si_bajaPoliza)) { dialog, _ ->
                viewModelDetallePoliza.darBajaPoliza(poliza) { exito ->
                    if (exito) {
                        findNavController().navigateUp()
                    }
                }
                dialog.dismiss()
            }
            builder.setNegativeButton(v.context.getString(R.string.boton_no_bajaPoliza)) { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }




    }
}