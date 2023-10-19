package com.example.ortseguros.fragments.home.misPolizas


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Pago
import com.example.ortseguros.entities.Poliza
import com.example.ortseguros.entities.Usuario
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

    fun realizarPago(pago: Pago, poliza: Poliza, callback: (Boolean) -> Unit) {
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
                    val pagos = polizaData.pagos
                    val pagoEncontrado = pagos.find { it.numeroPago == pago.numeroPago }

                    if (pagoEncontrado != null) {
                            pagoEncontrado.abonado = true
                            pagoEncontrado.fechaPago = obtenerFechaActual()

                            poliza.reference.set(polizaData)
                                .addOnSuccessListener {
                                    _toastMessage.value = "Pago realizado con éxito"
                                    callback(true)
                                }
                                .addOnFailureListener { exception ->
                                    exception.printStackTrace()
                                    callback(false)
                                }

                    }
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(false)
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
        numeroDeTarjeta: String,
        fechaDeCaducidad: String,
        titular: String,
        dniRealizarPago: String,
        codigoDeSeguridad: String
    ): Boolean {
        val isValid = numeroDeTarjeta.isNotBlank() &&
                fechaDeCaducidad.isNotBlank() &&
                titular.isNotBlank() &&
                dniRealizarPago.isNotBlank() &&
                codigoDeSeguridad.isNotBlank()

        if (!isValid) {
            _toastMessage.value  = "Todos los campos deben estar completos."
        }

        return isValid
    }


    fun obtenerTarjetaDeCreditoUsuario(callback: (Usuario.TarjetaDeCredito?) -> Unit) {
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        val userId = user?.uid.toString()

        val usuariosRef = db.collection("usuarios").document(userId)

        usuariosRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.toObject(Usuario::class.java)
                    val tarjetaCredito = userData?.tarjetaDeCredito
                    callback(tarjetaCredito)
                } else {
                    // El documento del usuario no existe, manejarlo según tus necesidades
                    callback(null)
                }
            }
            .addOnFailureListener {
                // Manejar el error según tus necesidades
                callback(null)
            }
    }




}