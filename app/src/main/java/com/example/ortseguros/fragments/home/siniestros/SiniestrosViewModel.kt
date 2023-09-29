package com.example.ortseguros.fragments.home.siniestros


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Siniestro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SiniestrosViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    private val _siniestrosLiveData = MutableLiveData<List<Siniestro>>()
    val siniestrosLiveData: LiveData<List<Siniestro>> = _siniestrosLiveData



    fun obtenerSiniestros() {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        val userId = user?.uid.toString()


        val listaSiniestros = mutableListOf<Siniestro>()

        val siniestrosRef = db.collection("siniestros")
            .whereEqualTo("idUsuario", userId)

        siniestrosRef.get()
            .addOnSuccessListener { snapshot ->
                for (siniestro in snapshot) {
                    val siniestroData = siniestro.toObject(Siniestro::class.java)
                    listaSiniestros.add(siniestroData)
                }
                _siniestrosLiveData.value = listaSiniestros
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                _siniestrosLiveData.value = emptyList() // Devuelve una lista vacía en caso de error
            }
    }
}