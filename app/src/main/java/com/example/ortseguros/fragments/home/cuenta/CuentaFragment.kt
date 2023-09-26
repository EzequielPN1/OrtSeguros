package com.example.ortseguros.fragments.home.cuenta

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CuentaFragment : Fragment() {



    private lateinit var viewModel: CuentaViewModel
    lateinit var v: View
    lateinit var btnTextCerrarSesion: TextView
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_cuenta, container, false)
        btnTextCerrarSesion = v.findViewById(R.id.btnTxtCerrarSesion)
        firebaseAuth = Firebase.auth
        viewModel = ViewModelProvider(this)[CuentaViewModel::class.java]
        return v
    }



    override fun onStart() {
        super.onStart()


        btnTextCerrarSesion.setOnClickListener {

            firebaseAuth.signOut()

            Toast.makeText(context, "Sesion Cerrada Correctamente", Toast.LENGTH_SHORT).show()
            val sharedPrefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().putBoolean("isLoggedIn", false).apply()

            val action = CuentaFragmentDirections.actionCuentaFragmentToLoginActivity()
            findNavController().navigate(action)


            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Aquí puedes realiza una accion
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

            requireActivity().finish()
        }
    }


}