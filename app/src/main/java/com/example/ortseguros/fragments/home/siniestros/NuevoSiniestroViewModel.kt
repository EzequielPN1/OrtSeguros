package com.example.ortseguros.fragments.home.siniestros

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Siniestro

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NuevoSiniestroViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    private val patentesList = ArrayList<String>()


    val selectedDateLiveData = MutableLiveData<String>()
    fun onDateSelected(day: Int, month: Int, year: Int) {
        val fechaDeSiniestro = "$day/${month + 1}/$year"
        selectedDateLiveData.value = fechaDeSiniestro
    }



    val selectedTimeLiveData = MutableLiveData<String>()
    fun onTimeSelected(time:String){
        val horaSiniestro = Editable.Factory.getInstance().newEditable(time)
        selectedTimeLiveData.value = horaSiniestro.toString()
    }



    fun obtenerPatentesDesdeFirestore(callback: (List<String>?, String?) -> Unit) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser

        db.collection("polizas")  .whereEqualTo("idUsuario", user?.uid.toString())
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


    fun guardarNuevoSiniestro(
        patente: String,
        descripcion: String,
        fecha: String,
        hora: String,
        ubicacion: String,
        callback: (Boolean, String?) -> Unit
    ) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser


        buscarIdPoliza(patente) { idPoliza ->
            if (idPoliza != null) {

                val nuevoSiniestro = Siniestro(
                    id = "",
                    idUsuario = user?.uid.toString(),
                    idPoliza = idPoliza,
                    fecha = fecha,
                    hora = hora,
                    ubicacion = ubicacion,
                    descripcion = descripcion,
                    patente = patente,
                )
                db.collection("siniestros")
                    .add(nuevoSiniestro)
                    .addOnSuccessListener {
                        callback(true, null)
                    }
                    .addOnFailureListener { e ->
                        callback(false, e.message)
                    }
            } else {
                callback(false, "Póliza no encontrada")
            }
        }
    }

    private fun buscarIdPoliza(patente: String, callback: (String?) -> Unit) {
        val polizasCollection = db.collection("polizas")

        polizasCollection.whereEqualTo("patente", patente)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && !task.result!!.isEmpty) {
                    val documentSnapshot = task.result!!.documents[0]
                    val idPoliza = documentSnapshot.id
                    callback(idPoliza)
                } else {
                    callback(null)
                }
            }
    }





}
