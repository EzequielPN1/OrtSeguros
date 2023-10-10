package com.example.ortseguros.fragments.login

import android.app.Activity
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Usuario
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class RegisterViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth



    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }



    private val _createAccountSuccess = MutableLiveData<Boolean>()
    val createAccountSuccess: LiveData<Boolean>
        get() = _createAccountSuccess





    val selectedDateLiveData = MutableLiveData<String>()
    fun onDateSelected(day: Int, month: Int, year: Int) {
        val fechaNacString = "$day/${month + 1}/$year"
        selectedDateLiveData.value = fechaNacString
    }



    fun createAccount(
        email: String,
        contrasenia: String,
        activity: Activity,
        inputNombre: EditText,
        inputApellido: EditText,
        inputFechaNac: EditText,
        inputDni: EditText,
        inputDomicilio: EditText,
        inputTelefono: EditText
    ) {

        firebaseAuth = Firebase.auth

        firebaseAuth.createUserWithEmailAndPassword(email, contrasenia)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {

                    val user = firebaseAuth.currentUser

                    // Obtener el número del último usuario ingresado
                    db.collection("usuarios")
                        .orderBy("numCliente", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnCompleteListener { queryTask ->
                            if (queryTask.isSuccessful) {
                                val lastUser = queryTask.result?.documents?.firstOrNull()
                                val numClienteString = lastUser?.get("numCliente") as? String

                                val numCliente = (numClienteString?.toInt() ?: 0) + 1

                                val usuario = Usuario(
                                    numCliente.toString(),
                                    user?.uid.toString(),
                                    inputNombre.text.toString(),
                                    inputApellido.text.toString(),
                                    inputFechaNac.text.toString(),
                                    inputDni.text.toString(),
                                    inputDomicilio.text.toString(),
                                    user?.email.toString(),
                                    inputTelefono.text.toString(),

                                )

                                db.collection("usuarios").document(user?.uid.toString()).set(usuario)
                                    .addOnCompleteListener { databaseTask ->
                                        if (databaseTask.isSuccessful) {
                                            sendEmailVerification { emailVerificationTask ->
                                                if (emailVerificationTask.isSuccessful) {
                                                    _toastMessage.value = "Cuenta creada correctamente, email de confirmación enviado"
                                                    _createAccountSuccess.value = true
                                                } else {
                                                    _toastMessage.value = "Error al enviar la verificación por correo electrónico: ${emailVerificationTask.exception}"
                                                    _createAccountSuccess.value = false
                                                }
                                            }
                                        } else {
                                            _toastMessage.value = "Error al escribir en la base de datos: ${databaseTask.exception}"
                                            _createAccountSuccess.value = false
                                        }
                                    }
                            } else {
                                _toastMessage.value = "Error al obtener el último usuario: ${queryTask.exception}"
                                _createAccountSuccess.value = false
                            }
                        }
                } else {
                    _toastMessage.value = "Error, no se creó la cuenta"
                    _createAccountSuccess.value = false
                }
            }
    }





    private fun sendEmailVerification(completion: (Task<Void>) -> Unit) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                completion(task)
            }
    }




    fun validarCampos(
        inputNombre: TextView, inputApellido: TextView, inputFechaNac: TextView,
        inputDni: TextView, inputDomicilio: TextView, inputEmail: TextView,
        inputTelefono: TextView, inputContrasenia: TextView, inputConfirmarContrasenia: TextView
    ): Boolean {
        var camposValidos = true

        if (validarIngresoVacio(inputNombre)) {
            _toastMessage.value = "Por favor, ingrese su nombre."
            camposValidos = false
        } else if (validarIngresoVacio(inputApellido)) {
            _toastMessage.value = "Por favor, ingrese su apellido."
            camposValidos = false
        } else if (validarIngresoVacio(inputFechaNac)) {
            _toastMessage.value = "Por favor, ingrese su fecha de nacimiento."
            camposValidos = false
        } else if (validarEdad(inputFechaNac)) {
            _toastMessage.value = "Debe ser mayor de edad."
            camposValidos = false
        } else if (validarIngresoVacio(inputDni)) {
            _toastMessage.value = "Por favor, ingrese su dni."
            camposValidos = false
        } else if (validarIngresoVacio(inputDomicilio)) {
            _toastMessage.value = "Por favor, ingrese su domicilio."
            camposValidos = false
        } else if (validarIngresoVacio(inputEmail)) {
            _toastMessage.value = "Por favor, ingrese su email."
            camposValidos = false
        } else if (!validarEmail(inputEmail)) {
            _toastMessage.value = "Por favor, ingrese un email correcto."
            camposValidos = false
        } else if (validarIngresoVacio(inputTelefono)) {
            _toastMessage.value = "Por favor, ingrese su teléfono."
            camposValidos = false
        } else if (validarIngresoVacio(inputContrasenia)) {
            _toastMessage.value = "Por favor, ingrese su contraseña."
            camposValidos = false
        } else if (!validarContrasenia(inputContrasenia)) {
            _toastMessage.value =
                "Mínimo 6 caracteres, contenga al menos una letra y al menos un número."
            camposValidos = false
        } else if (!confirmarContrasenias(inputContrasenia, inputConfirmarContrasenia)) {
            _toastMessage.value = "Sus contraseñas deben coincidir."
            camposValidos = false
        }

        return camposValidos
    }


    private fun validarEdad(inputFechaNac: TextView): Boolean {
        val fechaNacimientoStr = inputFechaNac.text.toString()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        val fechaNacimiento: Date = dateFormat.parse(fechaNacimientoStr) ?: return false
        val fechaActual = Calendar.getInstance().time

        val diferenciaEnMillis = fechaActual.time - fechaNacimiento.time
        val edad = diferenciaEnMillis / (1000L * 60 * 60 * 24 * 365)

        return edad < 18

    }


    private fun validarIngresoVacio(editText: TextView): Boolean {
        return editText.text.toString().isEmpty()
    }

    private fun validarEmail(inputEmail: TextView): Boolean {
        val email = inputEmail.text.toString()
        return email.matches(Regex("^[a-zA-Z0-9._%+-]+@(hotmail\\.com|gmail\\.com)$"))
    }

    private fun validarContrasenia(inputContrasenia: TextView): Boolean {
        val contrasenia = inputContrasenia.text.toString()
        return contrasenia.matches(Regex("^(?=.*[a-zA-Z])(?=.*\\d).{6,}\$"))
    }

    private fun confirmarContrasenias(
        inputContrasenia: TextView,
        inputConfirmarContrasenia: TextView
    ): Boolean {
        val contrasenia = inputContrasenia.text.toString()
        val confirmarContrasenia = inputConfirmarContrasenia.text.toString()
        return contrasenia == confirmarContrasenia
    }


}