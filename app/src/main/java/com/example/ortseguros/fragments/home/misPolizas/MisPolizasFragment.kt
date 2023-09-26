package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ortseguros.R
import com.example.ortseguros.adapters.PolizaAdapter
import com.example.ortseguros.entities.Poliza
import com.example.ortseguros.entities.PolizaRepository

class MisPolizasFragment : Fragment() {



    private lateinit var viewModel: MisPolizasViewModel
    lateinit var recyclerPoliza: RecyclerView
    var repository : PolizaRepository = PolizaRepository()
    lateinit var adapter : PolizaAdapter

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_mis_polizas, container, false)
        viewModel = ViewModelProvider(this)[MisPolizasViewModel::class.java]
        recyclerPoliza = v.findViewById(R.id.recPoliza)
        return v
    }



    override fun onStart() {
        super.onStart()

        recyclerPoliza.layoutManager = LinearLayoutManager(context)
        adapter = PolizaAdapter(repository.polizas)
        recyclerPoliza.adapter = adapter

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Aquí puedes realiza una accion
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}