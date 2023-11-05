package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ortseguros.R
import com.example.ortseguros.adapters.PagoAdapter


class PagosFragment : Fragment() {

    private lateinit var viewModelPagos: PagosViewModel
    private lateinit var v: View
    private lateinit var recyclerPago: RecyclerView
    private lateinit var pagoAdapter : PagoAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v= inflater.inflate(R.layout.fragment_pagos, container, false)
        viewModelPagos = ViewModelProvider(this)[PagosViewModel::class.java]
        recyclerPago = v.findViewById(R.id.recycleViewPagos)



        return v
    }

    override fun onStart() {
        super.onStart()

        val poliza = PagosFragmentArgs.fromBundle(requireArguments()).Poliza

        recyclerPago.layoutManager = LinearLayoutManager(context)


        viewModelPagos.pagosLiveData.observe(viewLifecycleOwner) { pagos ->
            val pagosMutableList = pagos.toMutableList()


            pagoAdapter = PagoAdapter(pagosMutableList){position->

                viewModelPagos.validarPago(pagosMutableList[position],poliza) { exito ->

                if(exito){
                    val action = PagosFragmentDirections.actionPagosFragmentToRealizarPagoFragment(
                        pagosMutableList[position],
                        poliza
                    )
                    findNavController().navigate(action)
                }else{
                    Toast.makeText(requireContext(), v.context.getString(R.string.pago_ya_realizado), Toast.LENGTH_SHORT).show()
                }

                }
            }
            recyclerPago.adapter = pagoAdapter
        }



        viewModelPagos.obtenerPagos(poliza)





    }



}