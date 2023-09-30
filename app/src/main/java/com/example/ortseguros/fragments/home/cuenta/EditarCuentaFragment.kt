package com.example.ortseguros.fragments.home.cuenta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.ortseguros.R

class EditarCuentaFragment : Fragment() {


     lateinit var viewModelEditarCuenta : EditarCuentaViewModel
     lateinit var v : View
     lateinit var inputNombre : EditText
     lateinit var inputApellido : EditText
     lateinit var inputDni : EditText
     lateinit var inputDomicilio : EditText
     lateinit var inputTelefono : EditText
     lateinit var btnAplicarCambios : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_editar_cuenta, container, false)

        viewModelEditarCuenta = ViewModelProvider(this)[EditarCuentaViewModel::class.java]
        inputNombre = v.findViewById(R.id.inputNombre)
        inputApellido = v.findViewById(R.id.inputApellido)
        inputDni = v.findViewById(R.id.inputDni)
        inputDomicilio = v.findViewById(R.id.inputDomicilio)
        inputTelefono = v.findViewById(R.id.inputTelefono)
        btnAplicarCambios = v.findViewById(R.id.btnAplicarCambios)
        return v
    }

    override fun onStart() {
        super.onStart()
        viewModelEditarCuenta.usuarioData.observe(viewLifecycleOwner) { usuario ->
            // Actualizar los EditText con los datos del usuario
            inputNombre.setText(usuario.nombre)
            inputApellido.setText(usuario.apellido)
            inputDni.setText(usuario.dni)
            inputDomicilio.setText(usuario.domicilio)
            inputTelefono.setText(usuario.telefono)
        }
        btnAplicarCambios.setOnClickListener{
            // Actualizar los LiveData en el ViewModel con los valores de los EditText
            viewModelEditarCuenta.setNombre(inputNombre.text.toString())
            viewModelEditarCuenta.setApellido(inputApellido.text.toString())
            viewModelEditarCuenta.setDni(inputDni.text.toString())
            viewModelEditarCuenta.setDomicilio(inputDomicilio.text.toString())
            viewModelEditarCuenta.setTelefono(inputTelefono.text.toString())

            // Aplicar los cambios en Firebase
            viewModelEditarCuenta.aplicarCambios()
        }
    }

}