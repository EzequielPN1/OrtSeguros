package com.example.ortseguros.fragments.home.siniestros


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Mensaje
import com.example.ortseguros.entities.Siniestro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetalleSiniestroViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var numMensaje=0

    fun mostrarTituloMensaje(callback: (Boolean, String) -> Unit) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser

        if (user != null) {
            val userId = user.uid

            db.collection("usuarios")
                .document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val nombre = documentSnapshot.getString("nombre")
                        val apellido = documentSnapshot.getString("apellido")
                        if (nombre != null && apellido != null) {
                            val nombreCompleto = "$nombre $apellido"
                            val mensajeTitulo = "Estimado/a $nombreCompleto"
                            callback(true, mensajeTitulo)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    callback(false, "Error al obtener el usuario: $e")
                }
        }
    }



    fun mostrarMensajeActual(siniestro: Siniestro, callback: (Boolean, Mensaje) -> Unit) {
        val mensajesNoLeidos = siniestro.mensajes.filter { !it.estado }

        if (mensajesNoLeidos.isNotEmpty()) {
            // Si hay mensajes no leídos, muestra el mensaje con el número más bajo
            val mensajeNoLeido = mensajesNoLeidos.minByOrNull { it.numero }
            if (mensajeNoLeido != null) {
                actualizarEstadoMensajeEnFirebase(siniestro,mensajeNoLeido)
                callback(true, mensajeNoLeido)
            } else {
                callback(false, Mensaje()) // Maneja el caso en que no se encontró un mensaje
            }
        } else {
            // Si no hay mensajes no leídos, muestra el último mensaje
            val mensajeUltimo = siniestro.mensajes.maxByOrNull { it.numero }
            if (mensajeUltimo != null) {
                callback(true, mensajeUltimo)
            } else {
                callback(false, Mensaje()) // Maneja el caso en que no se encontró un mensaje
            }
        }
    }


    private fun actualizarEstadoMensajeEnFirebase(siniestro: Siniestro, mensaje: Mensaje) {
        val siniestroRef = db.collection("siniestros").document(siniestro.id)
        val mensajesArray = siniestro.mensajes
        val mensajeEncontrado = mensajesArray.find { it.numero == mensaje.numero }

        if (mensajeEncontrado != null) {
            mensajeEncontrado.estado = true
            siniestroRef.update("mensajes", mensajesArray)
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseError", "Error al actualizar el estado: $e")
                }
        }
    }


    fun obtenerMensajePorIndice(siniestro: Siniestro, indice: Int, callback: (Mensaje?) -> Unit) {
        val siniestroRef = db.collection("siniestros").document(siniestro.id)

        siniestroRef.get().addOnSuccessListener { siniestroSnapshot ->
            if (siniestroSnapshot.exists()) {
                val mensajesArray = siniestroSnapshot.toObject(Siniestro::class.java)?.mensajes

                if (mensajesArray != null && indice >= 0 && indice < mensajesArray.size) {
                    val mensaje = mensajesArray[indice]
                    callback(mensaje)
                }
            }
        }.addOnFailureListener {
            callback(null)
        }
    }







}