package com.example.ortseguros.fragments.home.misPolizas


import android.net.Uri
import com.google.firebase.firestore.Query
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Pago
import com.example.ortseguros.entities.Poliza
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class NuevaPolizaViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

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
        uriImageFrente:String,
        uriImageLatDer:String,
        uriImageLatIzq:String,
        uriImagePosterior:String,
        callback: (Boolean) -> Unit
    ) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser

        obtenerValorVehiculo(marcaModelo) { valor ->
            if (valor != null) {
                val fechaActual = obtenerFechaActual()
                val diferenciaEnAnios = calcularDiferenciaAnios(fechaAltaVehiculo, fechaActual)
                val valorSumaAsegurada = calcularSumaAsegurada(valor.toDouble(), diferenciaEnAnios)
                val cuotaMensual = calcularValorCuota(valorSumaAsegurada, respCivil, danioTotal, granizo, roboParcial, roboTotal)

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
                                pagos = generarPagos(),
                                uriImageFrente = uriImageFrente,
                                uriImageLatDer = uriImageLatDer,
                                uriImageLatIzq = uriImageLatIzq,
                                uriImagePosterior = uriImagePosterior,
                            )

                            db.collection("polizas")
                                .add(poliza)
                                .addOnSuccessListener { documentReference ->
                                    val idGenerado = documentReference.id

                                    // Actualizar el ID en la póliza misma
                                    db.collection("polizas")
                                        .document(idGenerado)
                                        .update("id", idGenerado)
                                        .addOnSuccessListener {
                                            callback(true)
                                            _toastMessage.value = "Póliza guardada con éxito"
                                        }
                                        .addOnFailureListener { e ->
                                            callback(false)
                                            _toastMessage.value = e.message
                                        }
                                }
                                .addOnFailureListener { e ->
                                    callback(false)
                                    _toastMessage.value = e.message
                                }
                        } else {
                            callback(false)
                            _toastMessage.value = "Error al obtener el último número de póliza: ${queryTask.exception}"
                        }
                    }
            } else {
                callback(false)
                _toastMessage.value = "No se pudo encontrar el valor del vehículo."
            }
        }
    }




    fun obtenerMarcasModelos(callback: (List<String>?, String?) -> Unit) {
         val marcasModelosList = ArrayList<String>()

        db.collection("Vehiculos")
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

    private fun calcularSumaAsegurada(valorVehiculo: Double, diferenciaAnios: Int): Double {
        val porcentajeAntiguedad = 0.005
        return valorVehiculo - (valorVehiculo * porcentajeAntiguedad * diferenciaAnios)
    }



    private fun obtenerValorVehiculo(marcaModeloParam: String, callback: (String?) -> Unit) {
        db.collection("Vehiculos")
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



  private  fun calcularValorCuota(
        sumaAsegurada: Double,
        respCivil: Boolean,
        danioTotal: Boolean,
        granizo: Boolean,
        incendioTotal: Boolean,
        roboHurtoTotal: Boolean

    ): Int {
        val porcentajeCuotaBase = 0.010
        var cuotaMensual = sumaAsegurada * porcentajeCuotaBase

        if (respCivil) {
            val porcentajeRespCivil = 0.5
            cuotaMensual += cuotaMensual * porcentajeRespCivil
        }

        if (danioTotal) {
            val porcentajeDanioTotal = 1
            cuotaMensual += cuotaMensual * porcentajeDanioTotal
        }

        if (granizo) {
          val porcentajeGranizo = 0.5
          cuotaMensual += cuotaMensual * porcentajeGranizo
        }

        if (incendioTotal) {
            val porcentajeIncendioTotal = 1
            cuotaMensual += cuotaMensual * porcentajeIncendioTotal
        }

        if (roboHurtoTotal) {
            val porcentajeRoboHurtoTotal =  1
            cuotaMensual += cuotaMensual * porcentajeRoboHurtoTotal
        }

        return cuotaMensual.toInt()
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
        uriImageFrente: String,
        uriImageLatIzq: String,
        uriImageLatDer: String,
        uriImagePosterior: String
    ): LiveData<Boolean> {
        val fechaActual = obtenerFechaActual()
        val camposValidosLiveData = MutableLiveData<Boolean>()

        val camposNoVacios = listOf(fechaAltaVehiculo, patente).all { it.isNotBlank() }
        if (!camposNoVacios) {
            _toastMessage.value = "Los campos fecha de alta y patente no pueden estar vacíos."
            camposValidosLiveData.value = false
        } else if (!esFechaValida(fechaAltaVehiculo, fechaActual)) {
            _toastMessage.value = "La fecha de alta debe ser menor o igual a la fecha actual."
            camposValidosLiveData.value = false
        } else if (!respCivil && !danioTotal && !granizo && !roboParcial && !roboTotal) {
            _toastMessage.value = "Tiene que seleccionar por lo menos una cobertura."
            camposValidosLiveData.value = false
        } else if (listOf(uriImageFrente, uriImageLatIzq, uriImageLatDer, uriImagePosterior).any { it.isEmpty() }) {
            _toastMessage.value = "Tiene que ingresar todas las fotos del vehiculo."
            camposValidosLiveData.value = false
        } else {
            verificarPatenteUnica(patente) { esUnica ->
                if (!esUnica) {
                    _toastMessage.value = "La patente ingresada ya está registrada en otra póliza."
                    camposValidosLiveData.value = false
                } else {
                    camposValidosLiveData.value = true
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

    private fun verificarPatenteUnica(patente: String, callback: (Boolean) -> Unit) {
        db.collection("polizas")
            .whereEqualTo("patente", patente)
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



    private fun generarPagos(): List<Pago> {
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
                abonado = false
            )
            pagos.add(nuevoPago)


            calendar.add(Calendar.MONTH, 1)
        }
        return pagos
    }





    private val storage = Firebase.storage
    private val storageRef = storage.getReferenceFromUrl("gs://apportseguros-c6dea.appspot.com")


    fun cargarImagenEnFirestore(uri: Uri, onSuccess: (String) -> Unit) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser

        val imageName = "images/$user?.uid.toString()/${System.currentTimeMillis()}_${uri.lastPathSegment}"
        val imageRef: StorageReference = storageRef.child(imageName)
        val uploadTask: UploadTask = uri.let { imageRef.putFile(it) }

        uploadTask.addOnSuccessListener {
            onSuccess(imageName)
        }
    }


    fun eliminarImagenEnFirestore(imagePath: String, onComplete: (Boolean) -> Unit) {
        val imageRef = storageRef.child(imagePath)
        imageRef.delete()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }















}