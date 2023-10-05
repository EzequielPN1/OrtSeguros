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
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.utils.DatePickerFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class NuevaPolizaFragment : Fragment() {

    private lateinit var viewModelNuevaPoliza: NuevaPolizaViewModel
    private lateinit var v: View
    private lateinit var spinnerMarcaModelo: Spinner
    private lateinit var inputFechaAltaVehiculo: EditText
    private lateinit var inputPatente: EditText
    private lateinit var swcRespCivil: Switch
    private lateinit var swcDanioTotal: Switch
    private lateinit var swcGranizo: Switch
    private lateinit var swcRoboParcial: Switch
    private lateinit var swcRoboTotal: Switch
    private lateinit var btnNuevaPoliza: Button

    private lateinit var btnImage : Button
    private lateinit var imageView: ImageView


    private val storage = Firebase.storage





     private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
       if(uri != null){
          imageView.setImageURI(uri)
       }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_nueva_poliza, container, false)
        viewModelNuevaPoliza = ViewModelProvider(this)[NuevaPolizaViewModel::class.java]
        spinnerMarcaModelo = v.findViewById(R.id.spinner_marcaModelo)
        inputFechaAltaVehiculo  = v.findViewById(R.id.inputFechaAltaVehiculo)
        inputPatente = v.findViewById(R.id.inputPatente)
        swcRespCivil = v.findViewById(R.id.swc_respCivil)
        swcDanioTotal = v.findViewById(R.id.swc_danioTotal)
        swcGranizo = v.findViewById(R.id.swc_granizo)
        swcRoboParcial = v.findViewById(R.id.swc_roboParcial)
        swcRoboTotal  = v.findViewById(R.id.swc_roboTotal)
        btnNuevaPoliza = v.findViewById(R.id.btnNuevaPoliza)

        btnImage = v.findViewById(R.id.btnNuevaImagen)
        imageView = v.findViewById(R.id.imageView)

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
                spinnerMarcaModelo.adapter = adapter
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


        btnImage.setOnClickListener(){
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        inputFechaAltaVehiculo.setOnClickListener {
            val datePicker = DatePickerFragment { day, month, year ->
                viewModelNuevaPoliza.onDateSelected(day, month, year)
            }
            datePicker.show(childFragmentManager, "datePicker")
        }



        btnNuevaPoliza.setOnClickListener {

            val marcaModelo = spinnerMarcaModelo.selectedItem.toString()
            val fechaAltaVehiculo = inputFechaAltaVehiculo.text.toString()
            val patente = inputPatente.text.toString()
            val respCivil = swcRespCivil.isChecked
            val danioTotal = swcDanioTotal.isChecked
            val granizo = swcGranizo.isChecked
            val roboParcial = swcRoboParcial.isChecked
            val roboTotal = swcRoboTotal.isChecked

            viewModelNuevaPoliza.validarCampos(
                fechaAltaVehiculo,
                patente,
                respCivil,
                danioTotal,
                granizo,
                roboParcial,
                roboTotal
                ).observe(viewLifecycleOwner) { camposValidos ->
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