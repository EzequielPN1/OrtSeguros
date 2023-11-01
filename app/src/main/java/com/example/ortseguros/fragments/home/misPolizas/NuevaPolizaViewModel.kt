package com.example.ortseguros.fragments.home.misPolizas


import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.Query
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ortseguros.entities.Pago
import com.example.ortseguros.entities.Poliza
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.atomic.AtomicInteger

class NuevaPolizaViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    private val storage = Firebase.storage
    private val storageRef = storage.getReferenceFromUrl("gs://apportseguros-c6dea.appspot.com")

    val selectedDateLiveData = MutableLiveData<String>()
    fun onDateSelected(day: Int, month: Int, year: Int) {
        val fechaDeSiniestro = "$day/${month + 1}/$year"
        selectedDateLiveData.value = fechaDeSiniestro
    }

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }


    fun guardarNuevaPoliza(
        marcaModelo: String,
        fechaAltaVehiculo: String,
        patente: String,
        respCivil: Boolean,
        danioTotal: Boolean,
        granizo: Boolean,
        roboParcial: Boolean,
        roboTotal: Boolean,
        uriFrente: Uri?,
        uriLatIzq: Uri?,
        uriLatDer: Uri?,
        uriPosterior: Uri?,
        callback: (Boolean) -> Unit
    ) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser


        obtenerPorcentajeAntiguedad { porcentajeAntiguedad ->
            if (porcentajeAntiguedad != null) {

                obtenerValorVehiculo(marcaModelo) { valor ->
                    if (valor != null) {
                        val fechaActual = obtenerFechaActual()
                        val diferenciaEnAnios = calcularDiferenciaAnios(fechaAltaVehiculo, fechaActual)
                        val valorSumaAsegurada = calcularSumaAsegurada(valor.toDouble(), diferenciaEnAnios, porcentajeAntiguedad)

                        obtenerPorcentajesCoberturas { porcentajes ->
                            if (porcentajes != null) {
                                calcularValorCuota(
                                    valorSumaAsegurada,
                                    respCivil,
                                    danioTotal,
                                    granizo,
                                    roboParcial,
                                    roboTotal,
                                    porcentajes
                                ) { cuotaMensual ->
                                    if (cuotaMensual != null) {

                                        db.collection("polizas")
                                            .orderBy("numPoliza", Query.Direction.DESCENDING)
                                            .limit(1)
                                            .get()
                                            .addOnCompleteListener { queryTask ->
                                                if (queryTask.isSuccessful) {
                                                    val lastPoliza = queryTask.result?.documents?.firstOrNull()
                                                    val numPolizaString = lastPoliza?.get("numPoliza") as? String
                                                    val numPoliza = (numPolizaString?.toInt() ?: 0) + 1

                                                    val poliza = Poliza(
                                                        numPoliza = numPoliza.toString(),
                                                        idUsuario = user?.uid.toString(),
                                                        marcaModelo = marcaModelo,
                                                        fechaAltaVehiculo = fechaAltaVehiculo,
                                                        patente = patente,
                                                        fechaInicioPoliza = fechaActual,
                                                        sumaAsegurada = valorSumaAsegurada.toString(),
                                                        valorCuota = cuotaMensual.toString(),
                                                        respCivil = respCivil,
                                                        danioTotal = danioTotal,
                                                        granizo = granizo,
                                                        roboParcial = roboParcial,
                                                        roboTotal = roboTotal,
                                                        pagos = generarPagos(cuotaMensual.toString()),
                                                    )

                                                    db.collection("polizas")
                                                        .add(poliza)
                                                        .addOnSuccessListener { documentReference ->
                                                            val idGenerado = documentReference.id

                                                            val uriImageFrente = generarUrl(uriFrente, idGenerado)
                                                            val uriImageLatDer = generarUrl(uriLatIzq, idGenerado)
                                                            val uriImageLatIzq = generarUrl(uriLatDer, idGenerado)
                                                            val uriImagePosterior = generarUrl(uriPosterior, idGenerado)

                                                            // Actualizar el ID en la póliza misma
                                                            db.collection("polizas")
                                                                .document(idGenerado)
                                                                .update(
                                                                    "uriImagePredeterminada", uriImageFrente,
                                                                    "uriImageFrente", uriImageFrente,
                                                                    "uriImageLatDer", uriImageLatDer,
                                                                    "uriImageLatIzq", uriImageLatIzq,
                                                                    "uriImagePosterior", uriImagePosterior,
                                                                    "id", idGenerado
                                                                )
                                                                .addOnSuccessListener {
                                                                    val uris = listOf(
                                                                        uriFrente,
                                                                        uriLatIzq,
                                                                        uriLatDer,
                                                                        uriPosterior
                                                                    )
                                                                    val imageNames = listOf(
                                                                        uriImageFrente,
                                                                        uriImageLatIzq,
                                                                        uriImageLatDer,
                                                                        uriImagePosterior
                                                                    )

                                                                    cargarImagenes(uris, imageNames) {
                                                                        _toastMessage.value = "Póliza guardada con éxito"
                                                                        callback(true)
                                                                    }
                                                                }
                                                                .addOnFailureListener {
                                                                    callback(false)
                                                                }
                                                        }
                                                        .addOnFailureListener {
                                                            callback(false)
                                                        }
                                                }
                                            }
                                    } else {
                                        callback(false)
                                    }
                                }
                            } else {
                                callback(false)
                            }
                        }
                    } else {
                        callback(false)
                    }
                }
            } else {
                callback(false)
            }
        }
    }



    fun obtenerMarcasModelos(callback: (List<String>?, String?) -> Unit) {
        val marcasModelosList = ArrayList<String>()

        db.collection("vehiculos")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val marcaModelo = document.getString("marcaModelo")
                        if (marcaModelo != null) {
                            marcasModelosList.add(marcaModelo)
                        }
                    }
                    callback(marcasModelosList, null)
                } else {
                    callback(null, "Error al obtener las marcasModelosList")
                }
            }
    }

    private fun calcularDiferenciaAnios(fechaAlta: String, fechaActual: String): Int {
        val formato = SimpleDateFormat("dd/MM/yyyy")
        val fechaAltaDate = formato.parse(fechaAlta)
        val fechaActualDate = formato.parse(fechaActual)

        val calendarAlta = Calendar.getInstance()
        val calendarActual = Calendar.getInstance()
        calendarAlta.time = fechaAltaDate
        calendarActual.time = fechaActualDate

        return calendarActual.get(Calendar.YEAR) - calendarAlta.get(Calendar.YEAR)
    }

    private fun obtenerValorVehiculo(marcaModeloParam: String, callback: (String?) -> Unit) {
        db.collection("vehiculos")
            .whereEqualTo("marcaModelo", marcaModeloParam)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result!!.isEmpty) {
                        val vehiculoDocument = task.result!!.documents[0]
                        val valor = vehiculoDocument.getString("valor")
                        callback(valor)
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            }
    }

    private fun calcularSumaAsegurada(valorVehiculo: Double, diferenciaAnios: Int, porcentajeAntiguedad: Double): Double {
        return valorVehiculo - (valorVehiculo * porcentajeAntiguedad * diferenciaAnios)
    }


    private fun obtenerPorcentajeAntiguedad(callback: (Double?) -> Unit) {
        db.collection("valoresBase")
            .whereEqualTo("nombre", "antiguedad")
            .get()
            .addOnCompleteListener { queryTask ->
                if (queryTask.isSuccessful) {
                    val document = queryTask.result?.documents?.firstOrNull()
                    val porcentajeString = document?.get("porcentaje") as? String
                    val porcentaje = porcentajeString?.toDoubleOrNull()
                    callback(porcentaje)
                } else {
                    callback(null)
                }
            }
    }

    private fun calcularValorCuota(
        sumaAsegurada: Double,
        respCivil: Boolean,
        danioTotal: Boolean,
        granizo: Boolean,
        roboParcial: Boolean,
        roboTotal: Boolean,
        porcentajes: Map<String, Double>,
        callback: (Int) -> Unit
    ) {
        obtenerPorcentajeCuotaBase { porcentajeCuotaBase ->
            if (porcentajeCuotaBase != null) {
                var cuotaMensual = sumaAsegurada * porcentajeCuotaBase

                if (respCivil) {
                    val porcentajeRespCivil = porcentajes["respCivil"] ?: 0.0
                    cuotaMensual += cuotaMensual * porcentajeRespCivil
                }

                if (danioTotal) {
                    val porcentajeDanioTotal = porcentajes["danioTotal"] ?: 0.0
                    cuotaMensual += cuotaMensual * porcentajeDanioTotal
                }

                if (granizo) {
                    val porcentajeGranizo = porcentajes["granizo"] ?: 0.0
                    cuotaMensual += cuotaMensual * porcentajeGranizo
                }

                if (roboParcial) {
                    val porcentajeRoboParcial = porcentajes["roboParcial"] ?: 0.0
                    cuotaMensual += cuotaMensual * porcentajeRoboParcial
                }

                if (roboTotal) {
                    val porcentajeRoboTotal = porcentajes["roboTotal"] ?: 0.0
                    cuotaMensual += cuotaMensual * porcentajeRoboTotal
                }

                callback(cuotaMensual.toInt())
            } else {
                callback(0) // En caso de error
            }
        }
    }


    private fun obtenerPorcentajeCuotaBase(callback: (Double?) -> Unit) {
        db.collection("valoresBase")
            .whereEqualTo("nombre", "cuotaBase")
            .get()
            .addOnCompleteListener { queryTask ->
                if (queryTask.isSuccessful) {
                    val document = queryTask.result?.documents?.firstOrNull()
                    val porcentajeString = document?.get("porcentaje") as? String
                    val porcentaje = porcentajeString?.toDoubleOrNull()
                    callback(porcentaje)
                } else {
                    callback(null)
                }
            }
    }



    private fun obtenerPorcentajesCoberturas(callback: (Map<String, Double>?) -> Unit) {
        db.collection("coberturas")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val porcentajes = mutableMapOf<String, Double>()
                for (document in querySnapshot.documents) {
                    val cobertura = document.getString("nombre")
                    val porcentajeStr = document.getString("porcentaje")
                    if (cobertura != null && porcentajeStr != null) {
                        val porcentaje = porcentajeStr.toDoubleOrNull()
                        if (porcentaje != null) {
                            porcentajes[cobertura] = porcentaje
                        }
                    }
                }
                callback(porcentajes)
            }
            .addOnFailureListener {
                callback(null)
            }
    }


    private fun obtenerFechaActual(): String {
        val timeZone = TimeZone.getTimeZone("America/Argentina/Buenos_Aires")
        val calendar = Calendar.getInstance(timeZone)

        val formato = SimpleDateFormat("dd/MM/yyyy")
        formato.timeZone = timeZone

        return formato.format(calendar.time)
    }

    fun validarCampos(
        fechaAltaVehiculo: String,
        patente: String,
        respCivil: Boolean,
        danioTotal: Boolean,
        granizo: Boolean,
        roboParcial: Boolean,
        roboTotal: Boolean,
        uriFrente: Uri?,
        uriLatIzq: Uri?,
        uriLatDer: Uri?,
        uriPosterior: Uri?
    ): LiveData<Boolean> {
        val fechaActual = obtenerFechaActual()
        val camposValidosLiveData = MutableLiveData<Boolean>()

        // Validación de URIs
        if (uriFrente == null || uriLatIzq == null || uriLatDer == null || uriPosterior == null) {
            _toastMessage.value = "Por favor, cargue todas las imágenes."
            camposValidosLiveData.value = false
            return camposValidosLiveData
        }

        // Validación de fecha y patente
        val camposNoVacios = listOf(fechaAltaVehiculo, patente).all { it.isNotBlank() }

        if (!camposNoVacios) {
            _toastMessage.value = "Los campos fecha de alta y patente no pueden estar vacíos."
            camposValidosLiveData.value = false
        } else if (!esFechaValida(fechaAltaVehiculo, fechaActual)) {
            _toastMessage.value = "La fecha de alta debe ser menor o igual a la fecha actual."
            camposValidosLiveData.value = false
        } else {
            // Validación de booleanos
            if (!respCivil && !danioTotal && !granizo && !roboParcial && !roboTotal) {
                _toastMessage.value = "Tiene que seleccionar por lo menos una cobertura."
                camposValidosLiveData.value = false
            } else {
                verificarPatenteUnicaActiva(patente) { esUnica ->
                    if (!esUnica) {
                        _toastMessage.value =
                            "La patente ingresada ya está registrada en otra póliza."
                        camposValidosLiveData.value = false
                    } else {
                        verificarPatenteUnicaInactiva(patente) { esUnica ->
                            if (!esUnica) {
                                _toastMessage.value = "Patente a la espera de revision."
                                camposValidosLiveData.value = false
                            }else{
                                camposValidosLiveData.value = true
                            }

                        }
                    }
                }
            }

        }

        return camposValidosLiveData
    }


    private fun esFechaValida(fecha: String, fechaActual: String): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy")
        val fechaAltaDate = formato.parse(fecha)
        val fechaActualDate = formato.parse(fechaActual)

        return !fechaAltaDate.after(fechaActualDate)
    }

    private fun verificarPatenteUnicaActiva(patente: String, callback: (Boolean) -> Unit) {
        db.collection("polizas")
            .whereEqualTo("patente", patente)
            .whereEqualTo("activa", true)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val esUnica = task.result?.isEmpty ?: true
                    callback(esUnica)
                } else {
                    callback(false)
                }
            }
    }

    private fun verificarPatenteUnicaInactiva(patente: String, callback: (Boolean) -> Unit) {
        db.collection("polizas")
            .whereEqualTo("patente", patente)
            .whereEqualTo("activa", false)
            .whereEqualTo("eliminada", false)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val esUnica = task.result?.isEmpty ?: true
                    callback(esUnica)
                } else {
                    callback(false)
                }
            }
    }



    private fun generarPagos(precio: String): List<Pago> {
        val pagos = mutableListOf<Pago>()
        val formato = SimpleDateFormat("dd/MM/yyyy")
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, 5)

        for (numeroPago in 1..12) {
            // Crear un nuevo pago con el número de pago y la fecha de vencimiento
            val fechaVencimiento = formato.format(calendar.time)
            val nuevoPago = Pago(
                numeroPago = numeroPago.toString(),
                fechaVencimiento = fechaVencimiento,
                fechaPago = "",
                abonado = false,
                precio = precio,
            )
            pagos.add(nuevoPago)


            calendar.add(Calendar.MONTH, 1)
        }
        return pagos
    }



    private fun generarUrl(uri: Uri?, idGenerado: String): String {
        val user = Firebase.auth.currentUser
        return "images/${user?.uid.orEmpty()}/$idGenerado/${System.currentTimeMillis()}_${uri?.lastPathSegment.orEmpty()}"
    }


    private fun cargarImagenes(uris: List<Uri?>, imageNames: List<String>, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val deferredUploads =
                mutableListOf<Deferred<UploadTask.TaskSnapshot>>() // Cambio de tipo de retorno

            for (i in uris.indices) {
                val uri = uris[i]
                val imageName = imageNames[i]

                if (uri != null) {
                    deferredUploads.add(async {
                        val imageRef: StorageReference = storageRef.child(imageName)
                        val uploadTask = imageRef.putFile(uri)
                        uploadTask.await()
                    })
                } else {
                    Log.e("Carga de imagen", "Error: La URI de la imagen es nula para $imageName.")
                }
            }

            try {
                deferredUploads.awaitAll()
                withContext(Dispatchers.Main) {
                    callback()
                }
            } catch (e: Exception) {
                // Manejar cualquier error aquí
            }
        }
    }


}