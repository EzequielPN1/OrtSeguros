package com.example.ortseguros.fragments.login

import android.widget.TextView
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    fun validarCamposLogin(inputUsuario: TextView, inputContrasenia: TextView): String? {
        val ingresoUsuario = inputUsuario.text.toString()
        val ingresoContrasenia = inputContrasenia.text.toString()

        var mensajeError: String? = null

        if (ingresoUsuario.isEmpty() && ingresoContrasenia.isEmpty()) {
            mensajeError = "Por favor, ingrese sus datos."
        } else if (ingresoUsuario.isEmpty()) {
            mensajeError = "Por favor, ingrese su email."
        } else if (ingresoContrasenia.isEmpty()) {
            mensajeError = "Por favor, ingrese su contraseña."
        } else if (!validarEmail(inputUsuario)) {
            mensajeError = "Por favor, ingrese un email correcto."
        }

        return mensajeError
    }


    private fun validarEmail(inputUsuario: TextView): Boolean {
        val ingresoUsuario = inputUsuario.text.toString()
        val usuarioValido = ingresoUsuario.matches(Regex("^[a-zA-Z0-9._%+-]+@(hotmail\\.com|gmail\\.com)$"))
        return usuarioValido
    }

    fun validarOlvideEmail(inputUsuario: TextView): String? {
        val ingresoUsuario = inputUsuario.text.toString()
        var mensajeError: String? = null

        if (ingresoUsuario.isEmpty()) {
            mensajeError = "Por favor, ingrese un email."
        }
        if(validarEmail(inputUsuario)){
            mensajeError = "Por favor, ingrese un email correcto."
        }
        return mensajeError
    }


}