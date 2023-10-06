package com.example.ortseguros.fragments.home.misPolizas

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Pago
import com.example.ortseguros.entities.Poliza
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class RealizarPagoViewModel : ViewModel() {


    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }

    fun realizarPago(pago: Pago,poliza: Poliza) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        val userId = user?.uid.toString()

        val polizasRef = db.collection("polizas")
            .whereEqualTo("idUsuario", userId)
            .whereEqualTo("id", poliza.id)

        polizasRef.get()
            .addOnSuccessListener { snapshot ->
                for (poliza in snapshot) {
                    val polizaData = poliza.toObject(Poliza::class.java)
                    // Obtener la lista de pagos de la poliza
                    val pagos = polizaData.pagos

                    // Buscar el pago con el número proporcionado
                    val pagoEncontrado = pagos.find { it.numeroPago == pago.numeroPago }

                    if (pagoEncontrado != null) {
                        // Actualizar los atributos del pago
                        pagoEncontrado.abonado = true
                        pagoEncontrado.fechaPago = obtenerFechaActual()

                        // Actualizar la poliza en la base de datos
                        poliza.reference.set(polizaData)
                            .addOnSuccessListener {
                                // La poliza se actualizó con éxito
                                _toastMessage.value ="Pago realizado con exito"
                            }
                            .addOnFailureListener { exception ->
                                exception.printStackTrace()

                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()

            }
    }










private fun obtenerFechaActual(): String {
    val timeZone = TimeZone.getTimeZone("America/Argentina/Buenos_Aires")
    val calendar = Calendar.getInstance(timeZone)

    val formato = SimpleDateFormat("dd/MM/yyyy")
    formato.timeZone = timeZone

    return formato.format(calendar.time)
}











}