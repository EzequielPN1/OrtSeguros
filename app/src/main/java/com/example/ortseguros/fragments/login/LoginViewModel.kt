package com.example.ortseguros.fragments.login

import android.app.Activity
import android.content.Context
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }


    private val _signInSuccess = MutableLiveData<Boolean>()
    val signInSuccess: LiveData<Boolean>
        get() = _signInSuccess

    private lateinit var firebaseAuth: FirebaseAuth


    fun signIn(email: String, contrasenia: String, activity: Activity) {
        firebaseAuth = Firebase.auth
        firebaseAuth.signInWithEmailAndPassword(email, contrasenia)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser

                    val usuariosEmpresaRef = db.collection("usuariosEmpresa")
                    val uid = user?.uid

                    if (uid != null) {
                        usuariosEmpresaRef.document(uid).get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    _toastMessage.value = "Usuario no válido"
                                    _signInSuccess.value = false

                                } else {

                                    val verifica = user.isEmailVerified
                                    if (verifica == true) {
                                        val sharedPrefs = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                                        sharedPrefs.edit().putBoolean("isLoggedIn", true).apply()
                                        _signInSuccess.value = true
                                    } else {
                                        _toastMessage.value = "Usuario no verificado"
                                        _signInSuccess.value = false
                                    }

                                }

                            }
                    }
                } else {
                    _toastMessage.value = "Error de email y/0 contraseña"
                    _signInSuccess.value = false
                }
            }
    }


    fun sendPasswordReset(email:String,activity: Activity){
        firebaseAuth = Firebase.auth
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    _toastMessage.value = "Correo para cambio de contraseña enviado"
                } else {
                    _toastMessage.value = "Error, no se pudo realizar el proceso"
                }
            }

    }



    fun validarCamposLogin(inputUsuario: TextView, inputContrasenia: TextView): Boolean {
        val ingresoUsuario = inputUsuario.text.toString()
        val ingresoContrasenia = inputContrasenia.text.toString()

        var camposValidos = true

        if (ingresoUsuario.isEmpty() && ingresoContrasenia.isEmpty()) {
            _toastMessage.value = "Por favor, ingrese sus datos."
            camposValidos = false
        } else if (ingresoUsuario.isEmpty()) {
            _toastMessage.value = "Por favor, ingrese su email."
            camposValidos = false
        }else if (!validarEmail(inputUsuario)) {
            _toastMessage.value = "Por favor, ingrese un email correcto."
            camposValidos = false
        } else if (ingresoContrasenia.isEmpty()) {
            _toastMessage.value = "Por favor, ingrese su contraseña."
            camposValidos = false
        }

        return camposValidos
    }


    private fun validarEmail(inputUsuario: TextView): Boolean {
        val ingresoUsuario = inputUsuario.text.toString()
        return ingresoUsuario.matches(Regex("^[a-zA-Z0-9._%+-]+@(hotmail\\.com|gmail\\.com)$"))
    }

    fun validarOlvideEmail(inputUsuario: TextView): Boolean {
        val ingresoUsuario = inputUsuario.text.toString()

        var camposValidos = true

        if (ingresoUsuario.isEmpty()) {
            _toastMessage.value = "Por favor, ingrese un email."
            camposValidos = false
        } else if (!validarEmail(inputUsuario)) {
            _toastMessage.value = "Por favor, ingrese un email correcto."
            camposValidos = false
        }

        return camposValidos
    }
}