package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R


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
        inputSumaAsegurada.text = poliza.sumaAsegurada
        inputValorCuota.text = poliza.valorCuota



        btnPagos.setOnClickListener{

            val action = DetallePolizaFragmentDirections.actionDetallePolizaFragmentToPagosFragment(poliza)
            findNavController().navigate(action)



        }

    }
}