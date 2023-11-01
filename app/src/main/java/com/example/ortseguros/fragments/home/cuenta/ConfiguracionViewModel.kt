package com.example.ortseguros.fragments.home.cuenta

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConfiguracionViewModel : ViewModel() {
    private val db = Firebase.firestore


    fun obtenerTelefonoAsistenciaMecanica(onSuccess: (String) -> Unit) {
        val telefonosRef = db.collection("telefonos").whereEqualTo("nombre", "Asistencia mecanica")

        telefonosRef.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val document = snapshot.documents[0] // Tomar el primer documento
                    val telefono = document.getString("numero")
                    onSuccess(telefono!!)
                }
            }
    }


    fun obtenerTelefonoContacto(onSuccess: (String) -> Unit) {
        val telefonosRef = db.collection("telefonos").whereEqualTo("nombre", "Contacto")

        telefonosRef.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val document = snapshot.documents[0]
                    val telefono = document.getString("numero")
                    onSuccess(telefono!!)
                }
            }
    }



}