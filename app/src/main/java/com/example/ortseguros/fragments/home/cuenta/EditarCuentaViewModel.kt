package com.example.ortseguros.fragments.home.cuenta

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Usuario
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditarCuentaViewModel : ViewModel() {


    private val _usuarioData = MutableLiveData<Usuario>()
    private val db = Firebase.firestore
    val usuarioData: LiveData<Usuario> get() = _usuarioData

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val docRef = db.collection("usuarios").document(userId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val usuariodata = Usuario(document.getString("numCliente") ?: "",
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

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> get() = _nombre

    private val _apellido = MutableLiveData<String>()
    val apellido: LiveData<String> get() = _apellido

    private val _dni = MutableLiveData<String>()
    val dni: LiveData<String> get() = _dni

    private val _domicilio = MutableLiveData<String>()
    val domicilio: LiveData<String> get() = _domicilio

    private val _telefono = MutableLiveData<String>()
    val telefono: LiveData<String> get() = _telefono

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }


    fun aplicarCambios() {
        val userId = Firebase.auth.currentUser?.uid
        val nuevoNombre = _nombre.value // Obtener el valor del LiveData "_nombre"
        val nuevoApellido = _apellido.value
        val nuevoDni = _dni.value
        val nuevoDomicilio = _domicilio.value
        val nuevoTelefono = _telefono.value


        if (userId != null) {
            val docRef = db.collection("usuarios").document(userId)

            // Actualizar todos los campos en la base de datos con los nuevos valores
            val updates = mapOf(
                "nombre" to nuevoNombre,
                "apellido" to nuevoApellido,
                "dni" to nuevoDni,
                "domicilio" to nuevoDomicilio,
                "telefono" to nuevoTelefono
            )

            docRef
                .update(updates)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }
        setToastMessage("Cambios aplicados exitosamente")
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
    fun setTelefono(i: String) {
        _telefono.value = i
    }
    fun setDomicilio(i: String) {
        _domicilio.value = i
    }

    fun validarCampos(
        nombre: TextView, apellido: TextView,
        dni: TextView, domicilio: TextView, telefono: TextView): Boolean {
        var camposValidos = true

        if (validarIngresoVacio(nombre)) {
            _toastMessage.value = "Por favor, ingrese su nombre."
            camposValidos = false
        } else if (validarIngresoVacio(apellido)) {
            _toastMessage.value = "Por favor, ingrese su apellido."
            camposValidos = false
        } else if (validarIngresoVacio(dni)) {
            _toastMessage.value = "Por favor, ingrese su dni."
            camposValidos = false
        } else if (validarIngresoVacio(domicilio)) {
            _toastMessage.value = "Por favor, ingrese su domicilio."
            camposValidos = false
        } else if (validarIngresoVacio(telefono)) {
            _toastMessage.value = "Por favor, ingrese su teléfono."
            camposValidos = false
        }

        return camposValidos
    }

    private fun validarIngresoVacio(editText: TextView): Boolean {
        return editText.text.toString().isEmpty()
    }
}
