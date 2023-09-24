package com.example.ortseguros.fragments.Login

import android.widget.TextView
import androidx.lifecycle.ViewModel

class OlvidePassViewModel : ViewModel() {


    fun validarEmail(inputEmail: TextView): String? {
        val email = inputEmail.text.toString()

        if (!validarEmail(email)) {
            return "Por favor, ingrese un email correcto."
        }

        return null
    }

    private fun validarEmail(email: String): Boolean {
        val emailValido = email.matches(Regex("^[a-zA-Z0-9._%+-]+@(hotmail\\.com|gmail\\.com)$"))
        return emailValido
    }
}