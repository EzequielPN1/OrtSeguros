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
import org.w3c.dom.Text


class CuentaFragment : Fragment() {

    private lateinit var viewModelCuenta: CuentaViewModel
    lateinit var v: View
    lateinit var btnTextCerrarSesion: TextView
    lateinit var btnConfig : Button
    lateinit var db : FirebaseFirestore
    lateinit var txtNombre : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_cuenta, container, false)
        btnTextCerrarSesion = v.findViewById(R.id.btnTxtCerrarSesion)
        btnConfig = v.findViewById(R.id.btnConfig)
        db = Firebase.firestore
        txtNombre = v.findViewById(R.id.txtNombre)

        viewModelCuenta = ViewModelProvider(this)[CuentaViewModel::class.java]

        viewModelCuenta.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }


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



        val docRef = db.collection("usuarios").document("${Firebase.auth.currentUser?.uid}")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    txtNombre.text = document.getString("nombre")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }


}