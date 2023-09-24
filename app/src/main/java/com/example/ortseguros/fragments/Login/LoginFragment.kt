package com.example.ortseguros.fragments.Login

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

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    lateinit var v: View
    lateinit var buttonLogin: Button
    lateinit var inputUsuario: EditText
    lateinit var inputContrasenia: EditText
    lateinit var btnTextCrearCuenta: TextView
    lateinit var btnTextOlvideContrasenia: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener


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


        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Aquí puedes realiza una accion
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        buttonLogin.setOnClickListener {

            val mensajeError = viewModel.validarCamposLogin(inputUsuario, inputContrasenia)

            if (mensajeError != null) {
                ToastUtils.mostrarToast(context, mensajeError)
            } else {
                signIn(inputUsuario.text.toString(), inputContrasenia.text.toString())
            }

        }

        btnTextCrearCuenta.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        btnTextOlvideContrasenia.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToOlvidePassFragment()
            findNavController().navigate(action)
        }


    }


    private fun signIn(email: String, contrasenia: String) {

        firebaseAuth.signInWithEmailAndPassword(email, contrasenia)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    val user = firebaseAuth.currentUser
                    val verifica = user?.isEmailVerified

                    if (verifica == true) {

                        val sharedPrefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                        sharedPrefs.edit().putBoolean("isLoggedIn", true).apply()

                        val action = LoginFragmentDirections.actionLoginFragmentToHomeActivity()
                        findNavController().navigate(action)



                        requireActivity().finish()

                    } else {
                        Toast.makeText(context, "Usuario no verificado", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(context, "Error de email y/0 contraseña", Toast.LENGTH_SHORT)
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


