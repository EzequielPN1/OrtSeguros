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






    fun mostrarMensajeActual(siniestro: Siniestro, callback: (Boolean, String, String, String) -> Unit) {
        if (siniestro.mensajes.isNotEmpty()) {
            // Filtra los mensajes con estado false
            val mensajesNoLeidos = siniestro.mensajes.filter { !it.estado }

            if (mensajesNoLeidos.isNotEmpty()) {

                val mensajeNoLeido = mensajesNoLeidos.minByOrNull { it.numero }

                if (mensajeNoLeido != null) {
                    // Actualiza el estado en Firebase
                    actualizarEstadoMensajeEnFirebase(siniestro, mensajeNoLeido)

                    // Se encontró un mensaje no leído
                    callback(true, mensajeNoLeido.notificacion , "Asesor: ${mensajeNoLeido.usuarioEmpresa}",mensajeNoLeido.imagenURL)
                    return
                }
            }
        }

        // Si no hay mensajes no leídos o no se encontraron, devuelve el último mensaje (el mayor número en cadena)
        val mensajeUltimo = siniestro.mensajes.maxByOrNull { it.numero }
        if (mensajeUltimo != null) {
            callback(true, mensajeUltimo.notificacion, mensajeUltimo.usuarioEmpresa, mensajeUltimo.imagenURL)
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







}