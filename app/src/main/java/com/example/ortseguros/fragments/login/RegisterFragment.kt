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
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.entities.Usuario
import com.example.ortseguros.utils.DatePickerFragment
import com.example.ortseguros.utils.ToastUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {



    private lateinit var viewModelRegister: RegisterViewModel
    private lateinit var v: View
    private lateinit var inputNombre : EditText
    private lateinit var inputApellido : EditText
    private lateinit var inputFechaNac : EditText
    private lateinit var inputDni : EditText
    private lateinit var inputDomicilio : EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputTelefono: EditText
    private lateinit var inputContrasenia: EditText
    private lateinit var inputConfirmarContrasenia: EditText
    private lateinit var btnRegister: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

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

        firebaseAuth = Firebase.auth
        viewModelRegister = ViewModelProvider(this)[RegisterViewModel::class.java]
        return v
    }




    override fun onStart() {
        super.onStart()


        btnRegister.setOnClickListener {

            val mensajeError = viewModelRegister.validarCampos(inputNombre,inputApellido,inputFechaNac,inputDni,
                inputDomicilio,inputEmail,inputTelefono,inputContrasenia, inputConfirmarContrasenia)

            if (mensajeError != null) {
                ToastUtils.mostrarToast(context, mensajeError)
            } else {
                createAccount(inputEmail.text.toString(), inputContrasenia.text.toString())
            }

        }

        inputFechaNac.setOnClickListener {
            showDatePickerDialog()
        }

    }


//---------------------------------------------------fechaNac---------------------------
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment {day,month,year -> onDateSelected(day,month,year)}
        datePicker.show(childFragmentManager ,"datePicker")
    }
    private fun onDateSelected(day: Int, monthArg: Int, year: Int) {
        val month = monthArg + 1
        val fechaNacString = "$day/$month/$year"
        val editableFechaNac = Editable.Factory.getInstance().newEditable(fechaNacString)
        inputFechaNac.text = editableFechaNac
        // Toast.makeText(context, "La fecha es  " + inputFechaNac.text.toString(), Toast.LENGTH_SHORT).show()
    }

//-----------------------------------------------------------------------------------------------------------



    private fun createAccount(email: String, contrasenia: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, contrasenia)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    val user = firebaseAuth.currentUser

                    val usuario = Usuario(user?.uid.toString(),inputNombre.text.toString(),inputApellido.text.toString(),inputFechaNac.text .toString(),
                        inputDni.text.toString(),inputDomicilio.text.toString(),user?.email.toString(),inputTelefono.text.toString())

                    db.collection("usuarios").document(user?.uid.toString()).set(usuario)

                    sendEmailVerification()


                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    findNavController().navigate(action)

                } else {
                    Toast.makeText(
                        context,
                        "Algo salio mal, no se creo la cuenta" + task.exception,
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }

            }
    }

    //-------------------------------------------------------------------------------------
    private fun sendEmailVerification() {
        val user = firebaseAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Cuenta creada correctamente ,email de confirmacion enviado", Toast.LENGTH_SHORT)
                    .show()
            }else{
                Toast.makeText(context, "Error no se envio email de confirmacion", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

//-------------------------------------------------------------------------------------


}