package com.example.ortseguros.fragments.home.siniestros

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_siniestros, container, false)
        siniestroViewModel = ViewModelProvider(this)[SiniestrosViewModel::class.java]
        btnNuevoSiniestro = v.findViewById(R.id.btnNuevoSiniestro)
        recyclerSiniestro = v.findViewById(R.id.recSiniestro)

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

            siniestroAdapter = SiniestroAdapter(siniestrosMutableList){position->
                val action = SiniestrosFragmentDirections.actionSiniestrosFragmentToDetalleSiniestroFragment(siniestrosMutableList[position])
                findNavController().navigate(action)
            }
            recyclerSiniestro.adapter = siniestroAdapter
        }

        siniestroViewModel.obtenerSiniestros()
    }


}
