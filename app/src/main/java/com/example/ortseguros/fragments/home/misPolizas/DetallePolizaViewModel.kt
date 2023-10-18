package com.example.ortseguros.fragments.home.misPolizas

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ortseguros.entities.Poliza
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DetallePolizaViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }

    fun cargarImagenDesdeFirebase(uriImage: String, image: ImageView) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val imageRef = storageRef.child(uriImage)

        imageRef.downloadUrl.addOnSuccessListener { uri ->

            val imageUrl = uri.toString()
            // Cargar la imagen con Glide

            Glide.with(image.context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image)
            Log.e("MiApp", "Ok")
        }.addOnFailureListener { exception ->
            // Maneja cualquier error que ocurra al obtener la URL de descarga
            Log.e("MiApp", "Error al obtener la URL de descarga de la imagen: $exception")
        }
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



}