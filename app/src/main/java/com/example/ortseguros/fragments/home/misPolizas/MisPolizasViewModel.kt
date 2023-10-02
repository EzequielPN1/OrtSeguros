package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Poliza
import com.example.ortseguros.entities.Siniestro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MisPolizasViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    private val _polizasLiveData = MutableLiveData<List<Poliza>>()
    val polizasLiveData: LiveData<List<Poliza>> = _polizasLiveData



    fun obtenerPolizas() {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        val userId = user?.uid.toString()


        val listaPolizas = mutableListOf<Poliza>()

        val siniestrosRef = db.collection("polizas")
            .whereEqualTo("idUsuario", userId)

        siniestrosRef.get()
            .addOnSuccessListener { snapshot ->
                for (poliza in snapshot) {
                    val polizaData = poliza.toObject(Poliza::class.java)
                    listaPolizas.add(polizaData)
                }
                _polizasLiveData.value = listaPolizas
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                _polizasLiveData.value = emptyList()
            }
    }


}