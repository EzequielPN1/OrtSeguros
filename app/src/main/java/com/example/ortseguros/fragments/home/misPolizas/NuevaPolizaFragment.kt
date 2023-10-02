package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.utils.DatePickerFragment

class NuevaPolizaFragment : Fragment() {

    private lateinit var viewModelNuevaPoliza: NuevaPolizaViewModel
    private lateinit var v: View
    private lateinit var spinner_marcaModelo: Spinner
    private lateinit var inputFechaAltaVehiculo: EditText
    private lateinit var inputPatente: EditText
    private lateinit var swc_respCivil: Switch
    private lateinit var swc_danioTotal: Switch
    private lateinit var swc_granizo: Switch
    private lateinit var swc_roboParcial: Switch
    private lateinit var swc_roboTotal: Switch
    private lateinit var btnNuevaPoliza: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_nueva_poliza, container, false)
        viewModelNuevaPoliza = ViewModelProvider(this)[NuevaPolizaViewModel::class.java]
        spinner_marcaModelo = v.findViewById(R.id.spinner_marcaModelo)
        inputFechaAltaVehiculo  = v.findViewById(R.id.inputFechaAltaVehiculo)
        inputPatente = v.findViewById(R.id.inputPatente)
        swc_respCivil = v.findViewById(R.id.swc_respCivil)
        swc_danioTotal = v.findViewById(R.id.swc_danioTotal)
        swc_granizo = v.findViewById(R.id.swc_granizo)
        swc_roboParcial = v.findViewById(R.id.swc_roboParcial)
        swc_roboTotal  = v.findViewById(R.id.swc_roboTotal)
        btnNuevaPoliza = v.findViewById(R.id.btnNuevaPoliza)


        viewModelNuevaPoliza.selectedDateLiveData.observe(
            viewLifecycleOwner
        ) { fechaAltaVehiculo ->
            val editableFechaNac = Editable.Factory.getInstance().newEditable(fechaAltaVehiculo)
            inputFechaAltaVehiculo.text = editableFechaNac
        }


        viewModelNuevaPoliza.obtenerMarcasModelos { marcasModelos, error ->
            if (error == null && marcasModelos != null) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, marcasModelos)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_marcaModelo.adapter = adapter
            } else {
                Toast.makeText(requireContext(), error ?: "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        }


        viewModelNuevaPoliza.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModelNuevaPoliza.setToastMessage("")
            }
        }




        return v
    }


    override fun onStart() {
        super.onStart()

        inputFechaAltaVehiculo.setOnClickListener {
            val datePicker = DatePickerFragment { day, month, year ->
                viewModelNuevaPoliza.onDateSelected(day, month, year)
            }
            datePicker.show(childFragmentManager, "datePicker")
        }



        btnNuevaPoliza.setOnClickListener {

            val marcaModelo = spinner_marcaModelo.selectedItem.toString()
            val fechaAltaVehiculo = inputFechaAltaVehiculo.text.toString()
            val patente = inputPatente.text.toString()
            val respCivil = swc_respCivil.isChecked
            val danioTotal = swc_danioTotal.isChecked
            val granizo = swc_granizo.isChecked
            val roboParcial = swc_roboParcial.isChecked
            val roboTotal = swc_roboTotal.isChecked

            viewModelNuevaPoliza.validarCampos(fechaAltaVehiculo, patente).observe(viewLifecycleOwner) { camposValidos ->
                if (camposValidos) {
                    viewModelNuevaPoliza.guardarNuevaPoliza(
                        marcaModelo,
                        fechaAltaVehiculo,
                        patente,
                        respCivil,
                        danioTotal,
                        granizo,
                        roboParcial,
                        roboTotal
                    ) { exito, mensajeError ->
                        if (exito) {
                            Toast.makeText(
                                requireContext(),
                                "Póliza guardada con éxito",
                                Toast.LENGTH_SHORT
                            ).show()
                             findNavController().navigateUp()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Error al guardar la póliza: $mensajeError",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

















    }



}