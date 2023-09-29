package com.example.ortseguros.fragments.home.siniestros

import androidx.lifecycle.ViewModel
import com.example.ortseguros.entities.Siniestro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SiniestrosViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    fun obtenerSiniestros(): MutableList<Siniestro> {

        firebaseAuth = Firebase.auth


        val listaSiniestros = mutableListOf<Siniestro>()

        // Agregar siniestros a la lista de forma hardcodeada
        listaSiniestros.add(
            Siniestro(
                "1",
                "usuario1",
                "poliza1",
                "2023-09-30",
                "10:00 AM",
                "Ubicación 1"
            )
        )
        listaSiniestros.add(
            Siniestro(
                "2",
                "usuario1",
                "poliza2",
                "2023-09-30",
                "11:30 AM",
                "Ubicación 2"
            )
        )
        listaSiniestros.add(
            Siniestro(
                "3",
                "usuario2",
                "poliza1",
                "2023-10-01",
                "03:45 PM",
                "Ubicación 3"
            )
        )

        return listaSiniestros
    }












}