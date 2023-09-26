package com.example.ortseguros.fragments.home.cuenta

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class CuentaViewModel : ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage


    private lateinit var firebaseAuth: FirebaseAuth

    private val _signOutSuccess = MutableLiveData<Boolean>()
    val signOutSuccess: LiveData<Boolean>
        get() = _signOutSuccess

    fun cerrarSesion(activity: Activity) {

        firebaseAuth = Firebase.auth
        firebaseAuth.signOut()

        val sharedPrefs = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("isLoggedIn", false).apply()

        _toastMessage.value ="Sesion Cerrada Correctamente"

        _signOutSuccess.value = true
    }


}