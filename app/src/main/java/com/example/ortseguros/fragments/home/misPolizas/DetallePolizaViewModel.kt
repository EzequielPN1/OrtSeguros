package com.example.ortseguros.fragments.home.misPolizas


import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ortseguros.R
import com.example.ortseguros.entities.Poliza
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetallePolizaViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }


    fun darBajaPoliza(poliza: Poliza, callback: (Boolean) -> Unit) {
        val polizaRef = db.collection("polizas").document(poliza.id)

        polizaRef.update("activa", false)
            .addOnSuccessListener {
                // La póliza se ha dado de baja con éxito
                _toastMessage.value = "Póliza dada de baja con éxito"
                 callback(true)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                callback(false)
            }
    }

    fun determinarImagenesDeCobertura(poliza: Poliza, imageViews: Map<String, ImageView>) {
        val coberturas = mapOf(
            "danioTotal" to R.drawable.icon_danio_total,
            "respCivil" to R.drawable.icon_resp_civil,
            "granizo" to R.drawable.icon_granizo,
            "roboParcial" to R.drawable.icon_robo_parcial,
            "roboTotal" to R.drawable.icon_robo_total
        )

        for ((cobertura, imagenId) in coberturas) {
            val tieneCobertura = when (cobertura) {
                "danioTotal" -> poliza.danioTotal
                "respCivil" -> poliza.respCivil
                "granizo" -> poliza.granizo
                "roboParcial" -> poliza.roboParcial
                "roboTotal" -> poliza.roboTotal
                else -> false
            }

            if (tieneCobertura) {
                imageViews[cobertura]?.setImageResource(imagenId)
            }
        }
    }


    fun cambiarEstado(poliza: Poliza) {
        val polizaRef = db.collection("polizas").document(poliza.id)

        polizaRef.update("actualizada", false)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
            }
    }



}