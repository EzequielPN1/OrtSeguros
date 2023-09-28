package com.example.ortseguros.fragments.home.cuenta

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R

class ConfiguracionFragment : Fragment() {


    private lateinit var viewModel: ConfiguracionViewModel
    lateinit var v: View
    lateinit var btnEditarCuenta : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_configuracion, container, false)
        btnEditarCuenta = v.findViewById(R.id.btnEditarCuenta)
        return v

    }

    override fun onStart() {
        super.onStart()


        btnEditarCuenta.setOnClickListener{
            val action = ConfiguracionFragmentDirections.actionConfiguracionFragmentToEditarCuentaFragment()
            findNavController().navigate(action)
        }
    }

}