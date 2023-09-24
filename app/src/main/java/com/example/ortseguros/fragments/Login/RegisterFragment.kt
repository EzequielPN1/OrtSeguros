package com.example.ortseguros.fragments.Login

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

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModelRegister: RegisterViewModel
    lateinit var v: View
    lateinit var inputNombre : EditText
    lateinit var inputApellido : EditText
    lateinit var inputFechaNac : EditText
    lateinit var inputDni : EditText
    lateinit var inputDomicilio : EditText
    lateinit var inputEmail: EditText
    lateinit var inputTelefono: EditText
    lateinit var inputContrasenia: EditText
    lateinit var inputConfirmarContrasenia: EditText
    lateinit var btnRegister: Button
    private lateinit var firebaseAuth: FirebaseAuth
    val db = Firebase.firestore

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

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelRegister = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
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
    private fun onDateSelected(day:Int,month:Int,year:Int){
        val month = month + 1
        val fechaNacString = "$day/$month/$year"
        val editableFechaNac = Editable.Factory.getInstance().newEditable(fechaNacString)
        inputFechaNac.text = editableFechaNac
    //    Toast.makeText(context, "La fecha es  "+inputFechaNac.text.toString(), Toast.LENGTH_SHORT).show()
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




}