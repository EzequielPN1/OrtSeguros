package com.example.ortseguros.fragments.home.misPolizas

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DetallePolizaViewModel : ViewModel() {
    fun cargarImagenDesdeFirebase(uriImage: String, image: ImageView) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val imageRef = storageRef.child(uriImage)

        imageRef.downloadUrl.addOnSuccessListener { uri ->

            val imageUrl = uri.toString()
            // Cargar la imagen con Glide

            Glide.with(image.context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Opcional: puedes ajustar la estrategia de caché según tus necesidades
                .into(image)
            Log.e("MiApp", "Ok")
        }.addOnFailureListener { exception ->
            // Maneja cualquier error que ocurra al obtener la URL de descarga
            Log.e("MiApp", "Error al obtener la URL de descarga de la imagen: $exception")
        }
    }
}