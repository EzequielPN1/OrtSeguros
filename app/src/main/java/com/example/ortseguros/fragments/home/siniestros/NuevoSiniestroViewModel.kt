package com.example.ortseguros.fragments.home.siniestros

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Siniestro

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

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


    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun setToastMessage(message: String) {
        _toastMessage.value = message
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


    fun obtenerTipoSiniestrosFirestore(callback: (List<String>?, String?) -> Unit) {
        db.collection("tipoSiniestros")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val nombresArray = mutableListOf<String>() // Crear una lista para almacenar los nombres

                    for (document in task.result!!) {
                        val nombres = document.get("nombre") as? List<String>
                        if (nombres != null) {
                            nombresArray.addAll(nombres)
                        }
                    }
                    callback(nombresArray, null)
                } else {
                    callback(null, "Error al obtener los nombres")
                }
            }
    }

    fun guardarNuevoSiniestro(
        patente: String,
        descripcion: String,
        fecha: String,
        hora: String,
        ubicacion: String,
        tipoSiniestro:String,
        callback: (Boolean, String?) -> Unit
    ) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser


        buscarIdPoliza(patente) { idPoliza ->
            if (idPoliza != null) {

                val nuevoSiniestro = Siniestro(
                    idUsuario = user?.uid.toString(),
                    idPoliza = idPoliza,
                    fecha = fecha,
                    hora = hora,
                    ubicacion = ubicacion,
                    descripcion = descripcion,
                    patente = patente,
                    tipoSiniestro = tipoSiniestro,
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





    fun validarCampos(
        fechaSiniestro: String,
        hora: String,
        ubicacion: String,
    ): LiveData<Boolean> {
        val fechaActual = obtenerFechaActual()
        val camposValidosLiveData = MutableLiveData<Boolean>()

        if (fechaSiniestro.isEmpty()) {
            _toastMessage.value = "El campo fecha del siniestro no puede estar vacío."
            camposValidosLiveData.value = false
        } else if (!esFechaValida(fechaSiniestro, fechaActual)) {
            _toastMessage.value = "La fecha del siniestro debe ser menor o igual a la fecha actual."
            camposValidosLiveData.value = false
        } else if (hora.isEmpty()) {
            _toastMessage.value = "El campo hora no puede estar vacío."
            camposValidosLiveData.value = false
        } else if (ubicacion.isEmpty()) {
            _toastMessage.value = "El campo ubicación no puede estar vacío."
            camposValidosLiveData.value = false
        }  else {
            camposValidosLiveData.value = true
        }

        return camposValidosLiveData
    }







    private fun esFechaValida(fecha: String, fechaActual: String): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy")
        val fechaAltaDate = formato.parse(fecha)
        val fechaActualDate = formato.parse(fechaActual)

        return !fechaAltaDate.after(fechaActualDate)
    }
    private fun obtenerFechaActual(): String {
        val timeZone = TimeZone.getTimeZone("America/Argentina/Buenos_Aires")
        val calendar = Calendar.getInstance(timeZone)

        val formato = SimpleDateFormat("dd/MM/yyyy")
        formato.timeZone = timeZone

        return formato.format(calendar.time)
    }


}
