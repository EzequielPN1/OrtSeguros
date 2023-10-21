package com.example.ortseguros.fragments.home.siniestros


import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Siniestro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetalleSiniestroViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    fun mostrarTituloMesnaje(callback: (Boolean, String) -> Unit) {
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











    fun mostrarMensaje(siniestro: Siniestro, callback: (Boolean, String) -> Unit) {
        if (siniestro.mensaje.isNotEmpty()) {
            // El siniestro tiene un mensaje definido
            callback(true, siniestro.mensaje)
        } else {
            // Consultar la colección "coberturas" para obtener el mensaje predeterminado
            val mensaje = StringBuilder()

            db.collection("coberturas")
                .whereEqualTo("nombre", siniestro.tipoSiniestro)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            // Supongamos que hay un campo "descripcion" en el documento
                            val descripcion = document.getString("mensajeDefault")
                            if (descripcion != null) {
                                mensaje.append(descripcion)
                            }
                        }
                        val mensajeEncontrado = mensaje.isNotEmpty()
                        callback(mensajeEncontrado, mensaje.toString())
                    } else {
                        callback(false, "Error: No se pudo obtener el mensaje")
                    }
                }
        }
    }













}