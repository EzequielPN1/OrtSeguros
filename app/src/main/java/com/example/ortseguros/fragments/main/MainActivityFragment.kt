package com.example.ortseguros.fragments.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R


class MainActivityFragment : Fragment() {



    private lateinit var viewModel: MainActivityViewModel
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_main, container, false)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val isLoggedIn = viewModel.isLoggedIn(requireContext())


        if (isLoggedIn) {
            val action = MainActivityFragmentDirections.actionMainActivityFragmentToHomeActivity2()
            findNavController().navigate(action)
        } else {
            val action = MainActivityFragmentDirections.actionMainActivityFragmentToLoginActivity()
            findNavController().navigate(action)
        }


        requireActivity().finish()

        return v
    }



}