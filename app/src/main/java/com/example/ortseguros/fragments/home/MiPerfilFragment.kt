package com.example.ortseguros.fragments.home

import android.content.Context
import android.content.Intent
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
import com.example.ortseguros.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MiPerfilFragment : Fragment() {

    companion object {
        fun newInstance() = MiPerfilFragment()
    }

    private lateinit var viewModel: MiPerfilViewModel
    lateinit var v: View
    lateinit var btnTextCerrarSesion: TextView
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_mi_perfil, container, false)
        btnTextCerrarSesion = v.findViewById(R.id.btnTxtCerrarSesion)
        firebaseAuth = Firebase.auth
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MiPerfilViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onStart() {
        super.onStart()


        btnTextCerrarSesion.setOnClickListener {

            firebaseAuth.signOut()

            Toast.makeText(context, "Sesion Cerrada Correctamente", Toast.LENGTH_SHORT).show()
            val sharedPrefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().putBoolean("isLoggedIn", false).apply()

            val action = MiPerfilFragmentDirections.actionMiPerfilFragmentToLoginActivity2()
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