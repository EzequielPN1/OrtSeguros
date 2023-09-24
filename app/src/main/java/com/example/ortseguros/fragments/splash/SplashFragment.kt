package com.example.ortseguros.fragments.splash

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v= inflater.inflate(R.layout.fragment_splash, container, false)

        val sharedPrefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)


        v.postDelayed({

            if (isLoggedIn) {
                val action = SplashFragmentDirections.actionSplashFragmentToHomeActivity2()
                findNavController().navigate(action)

            } else {

               val action = SplashFragmentDirections.actionSplashFragmentToLoginActivity()
                findNavController().navigate(action)

            }

            requireActivity().finish()

        }, 2000)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        // TODO: Use the ViewModel
    }

}