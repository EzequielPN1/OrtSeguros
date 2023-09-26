package com.example.ortseguros.fragments.login

import android.content.Context
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
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.utils.ToastUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var v: View
    private lateinit var buttonLogin: Button
    private lateinit var inputUsuario: EditText
    private lateinit var inputContrasenia: EditText
    private lateinit var btnTextCrearCuenta: TextView
    private lateinit var btnTextOlvideContrasenia: TextView
    private lateinit var firebaseAuth: FirebaseAuth


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

        firebaseAuth = Firebase.auth

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        return v
    }



    override fun onStart() {
        super.onStart()

//-------------------------------------------------------------------------------------
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //  realiza una accion
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

//-------------------------------------------------------------------------------------


        buttonLogin.setOnClickListener {

            val mensajeError = viewModel.validarCamposLogin(inputUsuario, inputContrasenia)

            if (mensajeError != null) {
                ToastUtils.mostrarToast(context, mensajeError)
            } else {
                signIn(inputUsuario.text.toString(), inputContrasenia.text.toString())
            }

        }
//-------------------------------------------------------------------------------------

        btnTextCrearCuenta.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
//-------------------------------------------------------------------------------------

        btnTextOlvideContrasenia.setOnClickListener {

            val mensajeError = viewModel.validarOlvideEmail(inputUsuario)

            if (mensajeError != null) {
                ToastUtils.mostrarToast(context, mensajeError)
            } else {
                sendPasswordReset(inputUsuario.text.toString())
            }

        }

    }
//-------------------------------------------------------------------------------------

    private fun signIn(email: String, contrasenia: String) {

        firebaseAuth.signInWithEmailAndPassword(email, contrasenia)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    val user = firebaseAuth.currentUser
                    val verifica = user?.isEmailVerified

                    if (verifica == true) {

                        val sharedPrefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                        sharedPrefs.edit().putBoolean("isLoggedIn", true).apply()

                        val action = LoginFragmentDirections.actionLoginFragmentToMisPolizasActivity()
                        findNavController().navigate(action)


                    } else {
                        Toast.makeText(context, "Usuario no verificado", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(context, "Error de email y/0 contraseña", Toast.LENGTH_SHORT)
                        .show()
                }

            }


    }


    private fun sendPasswordReset(email:String){
        val activity = requireActivity()
        val context = requireContext()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(context, "Correo para cambio de contraseña enviado", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        "Error, no se pudo realizar el proceso",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
    }


    override fun onResume() {
        super.onResume()
        inputUsuario.text.clear()
        inputContrasenia.text.clear()
    }


}


