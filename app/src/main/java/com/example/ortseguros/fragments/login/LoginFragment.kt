package com.example.ortseguros.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var v: View
    private lateinit var buttonLogin: Button
    private lateinit var inputUsuario: EditText
    private lateinit var inputContrasenia: EditText
    private lateinit var btnTextCrearCuenta: TextView
    private lateinit var btnTextOlvideContrasenia: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_login, container, false)
        buttonLogin = v.findViewById(R.id.buttonLogin)
        inputUsuario = v.findViewById(R.id.inputEmailLogin)
        inputContrasenia = v.findViewById(R.id.textPassword)
        btnTextCrearCuenta = v.findViewById(R.id.btnTxtCrearCuenta)
        btnTextOlvideContrasenia = v.findViewById(R.id.btnTxtOlvidar)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                loginViewModel.setToastMessage("")
            }
        }

        return v
    }


    override fun onStart() {
        super.onStart()


        buttonLogin.setOnClickListener {
            if (loginViewModel.validarCamposLogin(inputUsuario, inputContrasenia)) {
                loginViewModel.signIn(inputUsuario.text.toString(), inputContrasenia.text.toString(), requireActivity())
                loginViewModel.signInSuccess.observe(viewLifecycleOwner) { success ->
                    if (success) {
                        val action = LoginFragmentDirections.actionLoginFragmentToMisPolizasActivity()
                        findNavController().navigate(action)
                        requireActivity().finish()
                    }
                }
            }
        }


        btnTextCrearCuenta.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }



        btnTextOlvideContrasenia.setOnClickListener {
            if (loginViewModel.validarOlvideEmail(inputUsuario)) {
                loginViewModel.sendPasswordReset(inputUsuario.text.toString(), requireActivity())
            }
        }
    }





}


