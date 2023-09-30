package com.example.ortseguros.fragments.home.cuenta

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CuentaFragment : Fragment() {

    private lateinit var viewModelCuenta: CuentaViewModel
    private lateinit var v: View
    private lateinit var btnTextCerrarSesion: TextView
    private lateinit var btnConfig : Button
    private lateinit var db : FirebaseFirestore
    private lateinit var txtNombre : TextView
    private lateinit var txtApellido : TextView
    private lateinit var txtDni : TextView
    private lateinit var txtEmail : TextView
    private lateinit var txtDomicilio : TextView
    private lateinit var txtTelefono : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_cuenta, container, false)


        viewModelCuenta = ViewModelProvider(this)[CuentaViewModel::class.java]

        viewModelCuenta.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        btnTextCerrarSesion = v.findViewById(R.id.btnTxtCerrarSesion)
        btnConfig = v.findViewById(R.id.btnConfig)
        db = Firebase.firestore
        txtNombre = v.findViewById(R.id.txtNombre)
        txtApellido = v.findViewById(R.id.txtApellido)
        txtDni = v.findViewById(R.id.txtDni)
        txtEmail = v.findViewById(R.id.txtEmail)
        txtDomicilio = v.findViewById(R.id.txtDomicilio)
        txtTelefono = v.findViewById(R.id.txtTelefono)




        return v
    }



    override fun onStart() {
        super.onStart()

        btnConfig.setOnClickListener{
            val action = CuentaFragmentDirections.actionCuentaFragmentToConfiguracionFragment()
            findNavController().navigate(action)
        }


        btnTextCerrarSesion.setOnClickListener {

            viewModelCuenta.cerrarSesion(requireActivity())
            viewModelCuenta.signOutSuccess.observe(viewLifecycleOwner) { success ->
                if (success) {
                    val action = CuentaFragmentDirections.actionCuentaFragmentToLoginActivity()
                    findNavController().navigate(action)

                    requireActivity().finish()
                }
            }
        }
        viewModelCuenta.loadUserData()
        viewModelCuenta.usuarioData.observe(viewLifecycleOwner) { usuario ->
            // Actualiza la vista con los datos del usuario
            txtNombre.text = usuario.nombre
            txtApellido.text = usuario.apellido
            txtDni.text = usuario.dni
            txtEmail.text = usuario.email
            txtDomicilio.text = usuario.domicilio
            txtTelefono.text = usuario.telefono
        }
    }
}