package com.example.ortseguros.fragments.home.misPolizas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Pago
import com.example.ortseguros.entities.Poliza
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PagosViewModel : ViewModel() {
    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    private val _pagosLiveData = MutableLiveData<List<Pago>>()
    val pagosLiveData: LiveData<List<Pago>> = _pagosLiveData



    fun obtenerPagos() {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        val userId = user?.uid.toString()

        val listaPagos = mutableListOf<Pago>()

        val polizasRef = db.collection("polizas")
            .whereEqualTo("idUsuario", userId)

        polizasRef.get()
            .addOnSuccessListener { snapshot ->
                for (poliza in snapshot) {
                    val polizaData = poliza.toObject(Poliza::class.java)
                    // Obtener la lista de pagos de la poliza
                    val pagos = polizaData.pagos
                    listaPagos.addAll(pagos)
                }
                // Actualizar el LiveData con la lista de pagos
                _pagosLiveData.value = listaPagos
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                _pagosLiveData.value = emptyList()
            }
    }


}