package com.example.ortseguros.fragments.home.siniestros

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
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.utils.DatePickerFragment
import com.example.ortseguros.utils.TimePickerFragment

class NuevoSiniestroFragment : Fragment() {

    private lateinit var viewModelNuevoSiniestro: NuevoSiniestroViewModel
    lateinit var v: View
    private lateinit var spinner: Spinner
    private lateinit var spinnerSiniestros:Spinner
    private lateinit var inputFecha: EditText
    private lateinit var inputHora: EditText
    private lateinit var inputUbicacion :EditText
    private lateinit var inputDescripcion : EditText
    private lateinit var btnNuevoSniestro : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_nuevo_siniestro, container, false)
        viewModelNuevoSiniestro = ViewModelProvider(this)[NuevoSiniestroViewModel::class.java]
        inputFecha = v.findViewById(R.id.inputFechaSiniestro)
        inputHora = v.findViewById(R.id.inputHoraSiniestro)
        inputUbicacion = v.findViewById(R.id.inputUbicacionNuevoSiniestro)
        inputDescripcion = v.findViewById(R.id.inputDescripcionNuevoSiniestro)
        btnNuevoSniestro = v.findViewById(R.id.btnGuardarNuevoSniestro)
        spinner = v.findViewById(R.id.spinner)
        spinnerSiniestros = v.findViewById(R.id.spinnerSiniestros)


        viewModelNuevoSiniestro.selectedDateLiveData.observe(
            viewLifecycleOwner
        ) { fechaSiniestro ->
            val editableFechaNac = Editable.Factory.getInstance().newEditable(fechaSiniestro)
            inputFecha.text = editableFechaNac
        }


        viewModelNuevoSiniestro.selectedTimeLiveData.observe(
            viewLifecycleOwner
        ) { horaSiniestro ->
            val editableText = Editable.Factory.getInstance().newEditable(horaSiniestro)
            inputHora.text = editableText
        }



        viewModelNuevoSiniestro.obtenerPatentesDesdeFirestore { patentes, error ->
            if (error == null && patentes != null) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, patentes)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            } else {
                Toast.makeText(requireContext(), error ?: "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        }


        viewModelNuevoSiniestro.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModelNuevoSiniestro.setToastMessage("")
            }
        }


        viewModelNuevoSiniestro.obtenerTipoSiniestrosFirestore { siniestros, error ->
            if (error == null && siniestros != null) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, siniestros)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerSiniestros.adapter = adapter
            } else {
                Toast.makeText(requireContext(), error ?: "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        }



        return v
    }






    override fun onStart() {
        super.onStart()


        inputFecha.setOnClickListener {
            val datePicker = DatePickerFragment { day, month, year ->
                viewModelNuevoSiniestro.onDateSelected(day, month, year)
            }
            datePicker.show(childFragmentManager, "datePicker")
        }


        inputHora.setOnClickListener{
            val timePicker = TimePickerFragment{time ->
                viewModelNuevoSiniestro.onTimeSelected(time)
            }
            timePicker.show(childFragmentManager,"time")
        }


        btnNuevoSniestro.setOnClickListener {

            val patente = spinner.selectedItem.toString()
            val fecha = inputFecha.text.toString()
            val hora = inputHora.text.toString()
            val ubicacion = inputUbicacion.text.toString()
            val descripcion = inputDescripcion.text.toString()
            val tipoSiniestro = spinnerSiniestros.selectedItem.toString()

            viewModelNuevoSiniestro.validarCampos(fecha, hora, ubicacion,patente,tipoSiniestro)
                .observe(viewLifecycleOwner) { camposValidos ->
                    if (camposValidos) {

                        viewModelNuevoSiniestro.guardarNuevoSiniestro(
                            patente,
                            descripcion,
                            fecha,
                            hora,
                            ubicacion,
                            tipoSiniestro
                        ) { exito, mensajeError, idGenerado ->
                            if (exito) {
                                Toast.makeText(
                                    requireContext(),
                                    "Siniestro guardado con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigateUp()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Error al guardar el siniestro: $mensajeError",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }

                }
        }


    }












}