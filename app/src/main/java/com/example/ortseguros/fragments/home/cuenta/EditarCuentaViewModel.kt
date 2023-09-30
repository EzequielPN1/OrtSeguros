package com.example.ortseguros.fragments.home.cuenta

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Usuario
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditarCuentaViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage


    private val _usuarioData = MutableLiveData<Usuario>()
    private val db = Firebase.firestore
    val usuarioData: LiveData<Usuario> get() = _usuarioData

    init {
        loadUserData()
    }

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
    // LiveData para cada campo de usuario
    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> get() = _nombre

    private val _apellido = MutableLiveData<String>()
    val apellido: LiveData<String> get() = _apellido

    private val _dni = MutableLiveData<String>()
    val dni: LiveData<String> get() = _dni

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _domicilio = MutableLiveData<String>()
    val domicilio: LiveData<String> get() = _domicilio

    private val _telefono = MutableLiveData<String>()
    val telefono: LiveData<String> get() = _telefono


    fun aplicarCambios() {
        val userId = Firebase.auth.currentUser?.uid
        val nuevoNombre = _nombre.value // Obtener el valor del LiveData "_nombre"
        val nuevoApellido = _apellido.value
        val nuevoDni = _dni.value
        val nuevoEmail = _email.value
        val nuevoDomicilio = _domicilio.value
        val nuevoTelefono = _telefono.value

        fun setNombre(nuevoNombre: String) {
            _nombre.value = nuevoNombre
        }

        if (userId != null) {
            val docRef = db.collection("usuarios").document(userId)

            // Actualizar todos los campos en la base de datos con los nuevos valores
            val updates = mapOf(
                "nombre" to nuevoNombre,
                "apellido" to nuevoApellido,
                "dni" to nuevoDni,
                "email" to nuevoEmail,
                "domicilio" to nuevoDomicilio,
                "telefono" to nuevoTelefono
            )

            docRef
                .update(updates)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }
    }

    fun setNombre(nuevoNombre: String) {
        _nombre.value = nuevoNombre
    }

    fun setApellido(i: String) {
        _apellido.value = i
    }
    fun setDni(i: String) {
        _dni.value = i
    }
    fun setEmail(i: String) {
        _email.value = i
    }
    fun setTelefono(i: String) {
        _telefono.value = i
    }
    fun setDomicilio(i: String) {
        _domicilio.value = i
    }
}
