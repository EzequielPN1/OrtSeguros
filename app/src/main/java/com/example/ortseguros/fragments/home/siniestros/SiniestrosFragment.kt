package com.example.ortseguros.fragments.home.siniestros

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.fragments.login.LoginFragmentDirections

class SiniestrosFragment : Fragment() {

    private lateinit var siniestroViewModel: SiniestrosViewModel
    lateinit var v: View

    private lateinit var btnNuevoSiniestro: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_siniestros, container, false)
        siniestroViewModel = ViewModelProvider(this)[SiniestrosViewModel::class.java]
        btnNuevoSiniestro = v.findViewById(R.id.btnNuevoSiniestro)



        return v
    }


    override fun onStart() {
        super.onStart()

        btnNuevoSiniestro.setOnClickListener(){
            val action = SiniestrosFragmentDirections.actionSiniestrosFragmentToNuevoSiniestroFragment()
            findNavController().navigate(action)
        }




    }

}