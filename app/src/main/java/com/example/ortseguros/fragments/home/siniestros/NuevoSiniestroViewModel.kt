package com.example.ortseguros.fragments.home.siniestros

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Siniestro
import com.google.firebase.firestore.Query
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
        val patentesList = ArrayList<String>()

        db.collection("polizas")  .whereEqualTo("idUsuario", user?.uid.toString())
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


    fun obtenerCoberturasFirestore(callback: (List<Pair<String, String>>?, String?) -> Unit) {
        db.collection("coberturas")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val coberturasList = mutableListOf<Pair<String, String>>() // Lista de pares (nombre, tituloSpinner)

                    for (document in task.result!!) {
                        val nombre = document.get("nombre") as? String
                        val titulo = document.get("tituloSpinner") as? String
                        if (nombre != null && titulo != null) {
                            coberturasList.add(Pair(nombre, titulo))
                        }
                    }
                    callback(coberturasList, null)
                } else {
                    callback(null, "Error al obtener las coberturas")
                }
            }
    }


    fun guardarNuevoSiniestro(
        patente: String,
        descripcion: String,
        fecha: String,
        hora: String,
        ubicacion: String,
        tipoSiniestro: String,
        callback: (Boolean, String?) -> Unit // Agrega un parámetro para el ID generado
    ) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser

        buscarIdPoliza(patente) { idPoliza ->
            if (idPoliza != null) {
                // Obtener el número del último siniestro
                db.collection("siniestros")
                    .orderBy("numSiniestro", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnCompleteListener { queryTask ->
                        if (queryTask.isSuccessful) {
                            val lastSiniestro = queryTask.result?.documents?.firstOrNull()
                            val numSiniestroString = lastSiniestro?.get("numSiniestro") as? String

                            val numSiniestro = (numSiniestroString?.toInt() ?: 0) + 1

                            val nuevoSiniestro = Siniestro(
                                numSiniestro = numSiniestro.toString(),
                                // No incluyas el campo 'id' aquí para que Firebase Firestore genere el ID automáticamente
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
                                .addOnSuccessListener { documentReference ->
                                    // Aquí obtienes el ID generado automáticamente por Firebase Firestore
                                    val idGenerado = documentReference.id

                                    // Asigna el ID generado al campo 'id' del siniestro
                                    db.collection("siniestros")
                                        .document(idGenerado)
                                        .update("id", idGenerado)
                                        .addOnSuccessListener {
                                            callback(true, null)
                                        }
                                        .addOnFailureListener { e ->
                                            callback(false, e.message)
                                        }
                                }
                                .addOnFailureListener { e ->
                                    callback(false, e.message)
                                }
                        } else {
                            callback(false, "Error al obtener el último número de siniestro: ${queryTask.exception}")
                        }
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
        patente: String,
        tipoSiniestro: String
    ): LiveData<Boolean> {
        val fechaActual = obtenerFechaActual()
        val camposValidosLiveData = MutableLiveData<Boolean>()

        contratoTipoSeguro(patente, tipoSiniestro) { contratoValido ->
            if (!contratoValido) {
                _toastMessage.value = "El tipo de seguro no fue contratado."
                camposValidosLiveData.value = false
            } else if (fechaSiniestro.isEmpty()) {
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
            } else {
                camposValidosLiveData.value = true
            }
        }

        return camposValidosLiveData
    }


    private fun contratoTipoSeguro(
        patente: String,
        tipoSiniestro: String,
        callback: (Boolean) -> Unit
    ) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser

        db.collection("polizas")
            .whereEqualTo("idUsuario", user?.uid.toString())
            .whereEqualTo("patente", patente)
            .get()
            .addOnSuccessListener { documents ->
                var contratoValido = false

                for (document in documents) {
                    val tipoSiniestroEncontrado = document.getBoolean(tipoSiniestro)
                    if (tipoSiniestroEncontrado != null && tipoSiniestroEncontrado) {
                        contratoValido = true
                    }

                }
                callback(contratoValido)
            }
            .addOnFailureListener {
                callback(false)
            }
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
