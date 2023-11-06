package com.example.ortseguros.fragments.home.cuenta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ortseguros.R

class EditarCuentaFragment : Fragment() {


     private lateinit var viewModelEditarCuenta : EditarCuentaViewModel
     private lateinit var v : View
     private lateinit var inputNombre : EditText
     private lateinit var inputApellido : EditText
     private lateinit var inputDni : EditText
     private lateinit var inputDomicilio : EditText
     private lateinit var inputTelefono : EditText
     private lateinit var btnAplicarCambios : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_editar_cuenta, container, false)

        viewModelEditarCuenta = ViewModelProvider(this)[EditarCuentaViewModel::class.java]
        inputNombre = v.findViewById(R.id.inputNombre)
        inputApellido = v.findViewById(R.id.inputApellido)
        inputDni = v.findViewById(R.id.inputDni)
        inputDomicilio = v.findViewById(R.id.inputDomicilio)
        inputTelefono = v.findViewById(R.id.inputTelefono)
        btnAplicarCambios = v.findViewById(R.id.btnAplicarCambios)

        viewModelEditarCuenta.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModelEditarCuenta.setToastMessage("")
            }
        }
        return v
    }

    override fun onStart() {
        super.onStart()
        viewModelEditarCuenta.usuarioData.observe(viewLifecycleOwner) { usuario ->

            inputNombre.setText(usuario.nombre)
            inputApellido.setText(usuario.apellido)
            inputDni.setText(usuario.dni)
            inputDomicilio.setText(usuario.domicilio)
            inputTelefono.setText(usuario.telefono)
        }
        btnAplicarCambios.setOnClickListener{

            if(viewModelEditarCuenta.validarCampos(inputNombre,inputApellido,inputDni,inputDomicilio,inputTelefono)){
            viewModelEditarCuenta.setNombre(inputNombre.text.toString())
            viewModelEditarCuenta.setApellido(inputApellido.text.toString())
            viewModelEditarCuenta.setDni(inputDni.text.toString())
            viewModelEditarCuenta.setDomicilio(inputDomicilio.text.toString())
            viewModelEditarCuenta.setTelefono(inputTelefono.text.toString())


            viewModelEditarCuenta.aplicarCambios()
            }
        }
    }

}