package com.example.ortseguros.fragments.home.misPolizas


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Poliza
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        callback: (Boolean, String?) -> Unit
    ) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser

        obtenerValorVehiculo(marcaModelo) { valor ->
            if (valor != null) {
                val fechaActual = obtenerFechaActual()

                val diferenciaEnAnios = calcularDiferenciaAnios(fechaAltaVehiculo, fechaActual)

                val valorSumaAsegurada = calcularSumaAsegurada(valor.toDouble(), diferenciaEnAnios)

                val cuotaMensual = calcularValorCuota(valorSumaAsegurada, respCivil, danioTotal, granizo, roboParcial, roboTotal)

                val poliza = Poliza(
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
                    roboTotal = roboTotal
                )


                db.collection("polizas")
                    .add(poliza)
                    .addOnSuccessListener {
                        callback(true, null)
                    }
                    .addOnFailureListener { e ->
                        callback(false, e.message)
                    }
            } else {

                callback(false, "No se pudo encontrar el valor del vehículo.")
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
        roboTotal: Boolean
    ): LiveData<Boolean> {
        val fechaActual = obtenerFechaActual()
        val camposValidosLiveData = MutableLiveData<Boolean>()


        if (fechaAltaVehiculo.isEmpty() || patente.isEmpty()) {
            _toastMessage.value = "Los campos fecha de alta y patente no pueden estar vacíos."
            camposValidosLiveData.value = false
            return camposValidosLiveData
        }

        verificarPatenteUnica(patente) { esUnica ->
            if (!esUnica) {
                _toastMessage.value = "La patente ingresada ya está registrada en otra póliza."
                camposValidosLiveData.value = false
            } else {
                if (!esFechaValida(fechaAltaVehiculo, fechaActual)) {
                    _toastMessage.value = "La fecha de alta debe ser menor o igual a la fecha actual."
                    camposValidosLiveData.value = false
                } else {
                    if (!respCivil && !danioTotal && !granizo && !roboParcial && !roboTotal) {
                        _toastMessage.value = "Tiene que seleccionar por lo menos una cobertura."
                        camposValidosLiveData.value = false
                    } else {
                        camposValidosLiveData.value = true
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


}