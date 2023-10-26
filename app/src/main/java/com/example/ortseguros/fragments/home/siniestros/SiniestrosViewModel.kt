package com.example.ortseguros.fragments.home.siniestros


import android.util.Log
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










    fun obtenerPatentesDesdeFirestore(callback: (List<String>?, String?) -> Unit) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        val patentesList = ArrayList<String>()

        db.collection("polizas")
            .whereEqualTo("idUsuario", user?.uid.toString())
            .whereEqualTo("activa", true)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val patente = document.getString("patente")
                        if (patente != null) {
                            patentesList.add(patente)
                        }
                    }
                    callback(patentesList, null)
                } else {
                    callback(null, "Error al obtener las patentes")
                }
            }
    }

    fun obtenerSiniestrosFiltrados(patenteSeleccionada: String, estadoSeleccionado: String?) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        val userId = user?.uid.toString()

        val listaSiniestros = mutableListOf<Siniestro>()


        if (estadoSeleccionado == "En revisión") {
            val siniestrosEnRevisionRef = db.collection("siniestros")
                .whereEqualTo("idUsuario", userId)
                .whereEqualTo("patente", patenteSeleccionada)
                .whereEqualTo("finalizado", false)

            siniestrosEnRevisionRef.get()
                .addOnSuccessListener { snapshot ->
                    for (siniestro in snapshot) {
                        val siniestroData = siniestro.toObject(Siniestro::class.java)
                        listaSiniestros.add(siniestroData)
                    }
                    _siniestrosLiveData.value = listaSiniestros
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    _siniestrosLiveData.value = emptyList()
                }


        } else if (estadoSeleccionado == "Finalizadas") {

            val siniestrosFinalizadasRef = db.collection("siniestros")
                .whereEqualTo("idUsuario", userId)
                .whereEqualTo("patente", patenteSeleccionada)
                .whereEqualTo("finalizado", true)


            siniestrosFinalizadasRef.get()
                .addOnSuccessListener { snapshot ->
                    for (siniestro in snapshot) {
                        val siniestroData = siniestro.toObject(Siniestro::class.java)
                        listaSiniestros.add(siniestroData)
                    }
                    _siniestrosLiveData.value = listaSiniestros
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    _siniestrosLiveData.value = emptyList()
                }


        }





    }




}