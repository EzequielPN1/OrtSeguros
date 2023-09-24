package com.example.ortseguros.fragments.Login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.utils.ToastUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class OlvidePassFragment : Fragment() {

    companion object {
        fun newInstance() = OlvidePassFragment()
    }

    private lateinit var viewModel: OlvidePassViewModel
    lateinit var v: View
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var inputEmail: EditText
    lateinit var buttonEnviar: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v=  inflater.inflate(R.layout.fragment_olvide_pass, container, false)
        inputEmail= v.findViewById(R.id.inputEmailOlvidePass)
        buttonEnviar= v.findViewById(R.id.btnOlvidePass)

        firebaseAuth = Firebase.auth

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OlvidePassViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        buttonEnviar.setOnClickListener {

            val mensajeError = viewModel.validarEmail(inputEmail)

            if (mensajeError != null) {
                ToastUtils.mostrarToast(context, mensajeError)
            } else {
                sendPasswordReset(inputEmail.text.toString())
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
                    val action = OlvidePassFragmentDirections.actionOlvidePassFragmentToLoginFragment()
                    findNavController().navigate(action)

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

}