package com.example.ortseguros.fragments.home.cuenta

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CuentaViewModel : ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private val _usuarioData = MutableLiveData<Usuario>()
    private val db = Firebase.firestore
    val usuarioData: MutableLiveData<Usuario> get() = _usuarioData


     fun loadUserData() {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val docRef = db.collection("usuarios").document(userId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val usuariodata = Usuario(
                            userId,
                            document.getString("nombre") ?: "",
                            document.getString("apellido") ?: "",
                            document.getString("fechaNac")?: "",
                            document.getString("dni")?: "",
                            document.getString("domicilio")?: "",
                            document.getString("email")?: "",
                            document.getString("telefono")?: ""
                        )
                        _usuarioData.value = usuariodata
                    } else {
                        _toastMessage.value = "No se encontraron datos de usuario."
                    }
                }
                .addOnFailureListener { exception ->
                    _toastMessage.value = "Error al cargar los datos del usuario: ${exception.message}"
                }
        } else {
            _toastMessage.value = "No se pudo obtener el ID del usuario."
        }
    }


    private lateinit var firebaseAuth: FirebaseAuth


    private val _signOutSuccess = MutableLiveData<Boolean>()
    val signOutSuccess: LiveData<Boolean>
        get() = _signOutSuccess



    fun cerrarSesion(activity: Activity) {

        firebaseAuth = Firebase.auth
        firebaseAuth.signOut()

        val sharedPrefs = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("isLoggedIn", false).apply()

        _toastMessage.value ="Sesion Cerrada Correctamente"
        _signOutSuccess.value = true
    }


}