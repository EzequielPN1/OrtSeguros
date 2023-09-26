package com.example.ortseguros.fragments.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.utils.DatePickerFragment


class RegisterFragment : Fragment() {

    private lateinit var viewModelRegister: RegisterViewModel
    private lateinit var v: View
    private lateinit var inputNombre: EditText
    private lateinit var inputApellido: EditText
    private lateinit var inputFechaNac: EditText
    private lateinit var inputDni: EditText
    private lateinit var inputDomicilio: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputTelefono: EditText
    private lateinit var inputContrasenia: EditText
    private lateinit var inputConfirmarContrasenia: EditText
    private lateinit var btnRegister: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_register, container, false)

        inputNombre = v.findViewById(R.id.inputNombre)
        inputApellido = v.findViewById(R.id.inputApellido)
        inputFechaNac = v.findViewById(R.id.inputFechaNac)
        inputDni = v.findViewById(R.id.inputDni)
        inputDomicilio = v.findViewById(R.id.inputDomicilio)
        inputEmail = v.findViewById(R.id.inputEmailRegister)
        inputTelefono = v.findViewById(R.id.inputTelefono)
        inputContrasenia = v.findViewById(R.id.inputContrasenia)
        inputConfirmarContrasenia = v.findViewById(R.id.inputConfirmarContrasenia)
        btnRegister = v.findViewById(R.id.btnRegister)


        viewModelRegister = ViewModelProvider(this)[RegisterViewModel::class.java]

        viewModelRegister.selectedDateLiveData.observe(
            viewLifecycleOwner,
            Observer { fechaNacString ->
                val editableFechaNac = Editable.Factory.getInstance().newEditable(fechaNacString)
                inputFechaNac.text = editableFechaNac
            })

        viewModelRegister.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        return v
    }


    override fun onStart() {
        super.onStart()

        inputFechaNac.setOnClickListener {
            val datePicker = DatePickerFragment { day, month, year ->
                viewModelRegister.onDateSelected(day, month, year)
            }
            datePicker.show(childFragmentManager, "datePicker")
        }



        btnRegister.setOnClickListener {

            if (viewModelRegister.validarCampos(
                    inputNombre,
                    inputApellido,
                    inputFechaNac,
                    inputDni,
                    inputDomicilio,
                    inputEmail,
                    inputTelefono,
                    inputContrasenia,
                    inputConfirmarContrasenia
                )
            ) {

                viewModelRegister.createAccount(
                    inputEmail.text.toString(),
                    inputContrasenia.text.toString(),
                    requireActivity(),
                    inputNombre,
                    inputApellido,
                    inputFechaNac,
                    inputDni,
                    inputDomicilio,
                    inputContrasenia
                )

                viewModelRegister.createAccountSuccess.observe(viewLifecycleOwner) { success ->
                    if (success) {
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }


}