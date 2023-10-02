package com.example.ortseguros.fragments.home.misPolizas

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
import com.example.ortseguros.adapters.PolizaAdapter


class MisPolizasFragment : Fragment() {

    private lateinit var viewModelPolizas: MisPolizasViewModel
    private lateinit var v: View
    private lateinit var btnAgregarPoliza: Button


    private lateinit var recyclerPoliza: RecyclerView
    private lateinit var polizaAdapter : PolizaAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_mis_polizas, container, false)
        viewModelPolizas = ViewModelProvider(this)[MisPolizasViewModel::class.java]
        btnAgregarPoliza = v.findViewById(R.id.btnAgregarPoliza)
        recyclerPoliza = v.findViewById(R.id.recPoliza)


        return v
    }





    override fun onStart() {
        super.onStart()

        btnAgregarPoliza.setOnClickListener {
            val action = MisPolizasFragmentDirections.actionMisPolizasFragmentToNuevaPolizaFragment()
            findNavController().navigate(action)
        }

        recyclerPoliza.layoutManager = LinearLayoutManager(context)


        viewModelPolizas.polizasLiveData.observe(viewLifecycleOwner) { polizas ->
            val polizasMutableList = polizas.toMutableList()

            polizaAdapter = PolizaAdapter(polizasMutableList){position->

                val action = MisPolizasFragmentDirections.actionMisPolizasFragmentToDetallePolizaFragment(polizasMutableList[position])
                findNavController().navigate(action)
            }
            recyclerPoliza.adapter = polizaAdapter
        }



        viewModelPolizas.obtenerPolizas()





    }

}