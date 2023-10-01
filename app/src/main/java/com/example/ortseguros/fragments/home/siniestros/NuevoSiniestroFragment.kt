package com.example.ortseguros.fragments.home.siniestros

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.ortseguros.R
import com.example.ortseguros.utils.DatePickerFragment
import com.example.ortseguros.utils.TimePickerFragment

class NuevoSiniestroFragment : Fragment() {


    private lateinit var viewModelNuevoSiniestro: NuevoSiniestroViewModel
    lateinit var v: View
    private lateinit var inputFecha: EditText
    private lateinit var inputHora: EditText




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_nuevo_siniestro, container, false)
        viewModelNuevoSiniestro = ViewModelProvider(this)[NuevoSiniestroViewModel::class.java]
        inputFecha = v.findViewById(R.id.inputFechaSiniestro)
        inputHora = v.findViewById(R.id.inputHoraSiniestro)



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





    }












}