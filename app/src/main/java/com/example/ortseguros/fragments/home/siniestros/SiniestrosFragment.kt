package com.example.ortseguros.fragments.home.siniestros

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ortseguros.R
import com.example.ortseguros.adapters.SiniestroAdapter


class SiniestrosFragment : Fragment() {

    private lateinit var siniestroViewModel: SiniestrosViewModel
    lateinit var v: View
    private lateinit var btnNuevoSiniestro: Button
    private lateinit var recyclerSiniestro: RecyclerView
    private lateinit var siniestroAdapter: SiniestroAdapter
    private lateinit var spinnerPatentesSiniestros: Spinner
    private lateinit var spinnerEstadoSiniestro: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_siniestros, container, false)
        siniestroViewModel = ViewModelProvider(this)[SiniestrosViewModel::class.java]
        btnNuevoSiniestro = v.findViewById(R.id.btnNuevoSiniestro)
        recyclerSiniestro = v.findViewById(R.id.recSiniestro)
        spinnerPatentesSiniestros = v.findViewById(R.id.spinnerPatentesSiniestros)
        spinnerEstadoSiniestro = v.findViewById(R.id.spinnerEstadoSiniestro)

        // Crea un ArrayAdapter utilizando los estados de siniestro desde los recursos
        val estadosArray = resources.getStringArray(R.array.siniestro_estados)
        val estadosAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estadosArray)

        estadosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstadoSiniestro.adapter = estadosAdapter

        //---

        siniestroViewModel.obtenerPatentesDesdeFirestore { patentes, error ->
            if (error == null && patentes != null) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, patentes)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPatentesSiniestros.adapter = adapter
            } else {
                Toast.makeText(requireContext(), error ?: "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        }

        //---

        spinnerPatentesSiniestros.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val patenteSeleccionada = spinnerPatentesSiniestros?.selectedItem?.toString()
                val estadoSeleccionado = spinnerEstadoSiniestro?.selectedItem?.toString()
                if (!patenteSeleccionada.isNullOrEmpty()) {
                    siniestroViewModel.obtenerSiniestrosFiltrados(patenteSeleccionada, estadoSeleccionado)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerEstadoSiniestro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val patenteSeleccionada = spinnerPatentesSiniestros?.selectedItem?.toString()
                val estadoSeleccionado = spinnerEstadoSiniestro?.selectedItem?.toString()
                if (!patenteSeleccionada.isNullOrEmpty() && !estadoSeleccionado.isNullOrEmpty()) {
                    siniestroViewModel.obtenerSiniestrosFiltrados(patenteSeleccionada, estadoSeleccionado)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }








        return v
    }

    override fun onStart() {
        super.onStart()

        btnNuevoSiniestro.setOnClickListener {
            val action = SiniestrosFragmentDirections.actionSiniestrosFragmentToNuevoSiniestroFragment()
            findNavController().navigate(action)
        }

        recyclerSiniestro.layoutManager = LinearLayoutManager(context)

        siniestroViewModel.siniestrosLiveData.observe(viewLifecycleOwner) { siniestros ->
            val siniestrosMutableList = siniestros.toMutableList()

            siniestroAdapter = SiniestroAdapter(siniestrosMutableList) { position ->
                val action = SiniestrosFragmentDirections.actionSiniestrosFragmentToDetalleSiniestroFragment(siniestrosMutableList[position])
                findNavController().navigate(action)
            }
            recyclerSiniestro.adapter = siniestroAdapter
        }


    }




}
