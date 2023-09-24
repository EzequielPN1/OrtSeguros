package com.example.ortseguros.fragments.Login

import android.widget.TextView
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    fun validarCamposLogin(inputUsuario: TextView, inputContrasenia: TextView): String? {
        val ingresoUsuario = inputUsuario.text.toString()
        val ingresoContrasenia = inputContrasenia.text.toString()

        if (ingresoUsuario.isEmpty() && ingresoContrasenia.isEmpty()) {
            return "Por favor, ingrese sus datos."
        } else if (ingresoUsuario.isEmpty()) {
            return "Por favor, ingrese su email."
        } else if (ingresoContrasenia.isEmpty()) {
            return "Por favor, ingrese su contraseña."
        } else if (!validarEmail(inputUsuario)) {
            return "Por favor, ingrese un email correcto."
        }

        return null
    }

    private fun validarEmail(inputUsuario: TextView): Boolean {
        val ingresoUsuario = inputUsuario.text.toString()
        val usuarioValido = ingresoUsuario.matches(Regex("^[a-zA-Z0-9._%+-]+@(hotmail\\.com|gmail\\.com)$"))
        return usuarioValido
    }

}