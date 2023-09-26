package com.example.ortseguros.fragments.login

import android.widget.TextView
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class RegisterViewModel : ViewModel() {

    fun validarCampos(inputNombre: TextView,inputApellido: TextView,inputFechaNac: TextView,inputDni: TextView,inputDomicilio: TextView,
                      inputEmail: TextView, inputTelefono: TextView,inputContrasenia: TextView, inputConfirmarContrasenia: TextView): String? {

        var mensajeError: String? = null

        if(validarIngresoVacio(inputNombre)){
            mensajeError = "Por favor, ingrese su nombre."
        }else if(validarIngresoVacio(inputApellido)){
            mensajeError = "Por favor, ingrese su apellido."
        }else  if(validarIngresoVacio(inputFechaNac)) {
            mensajeError = "Por favor, ingrese su fecha de nacimiento."
        }else  if(validarEdad(inputFechaNac)) {
            mensajeError = "Debe ser mayor de edad."
        }else if(validarIngresoVacio(inputDni)){
            mensajeError = "Por favor, ingrese su dni."
        }else if(validarIngresoVacio(inputDomicilio)){
            mensajeError = "Por favor, ingrese su domicilio."
        } else if (validarIngresoVacio(inputEmail)) {
            mensajeError = "Por favor, ingrese su email."
        } else if(validarIngresoVacio(inputTelefono)){
            mensajeError = "Por favor, ingrese su itelefono."
        }else if (!validarEmail(inputEmail)) {
            mensajeError = "Por favor, ingrese un email correcto."
        } else if (validarIngresoVacio(inputContrasenia)) {
            mensajeError = "Por favor, ingrese su contraseña."
        } else if (!validarContrasenia(inputContrasenia)) {
            mensajeError = "Mínimo 6 caracteres, contenga al menos una letra y al menos un número."
        } else if (!confirmarContrasenias(inputContrasenia, inputConfirmarContrasenia)) {
            mensajeError = "Error al validar, sus contraseñas deben coincidir."
        }

        return mensajeError
    }

    private fun validarEdad(inputFechaNac: TextView): Boolean {
        val fechaNacimientoStr = inputFechaNac.text.toString()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy") 

            val fechaNacimiento: Date = dateFormat.parse(fechaNacimientoStr) ?: return false
            val fechaActual = Calendar.getInstance().time

            val diferenciaEnMillis = fechaActual.time - fechaNacimiento.time
            val edad = diferenciaEnMillis / (1000L * 60 * 60 * 24 * 365)

            return edad < 18

    }




    private fun validarIngresoVacio(editText: TextView): Boolean {
        return editText.text.toString().isEmpty()
    }

    private fun validarEmail(inputEmail: TextView): Boolean {
        val email = inputEmail.text.toString()
        val emailValido = email.matches(Regex("^[a-zA-Z0-9._%+-]+@(hotmail\\.com|gmail\\.com)$"))
        return emailValido
    }

    private fun validarContrasenia(inputContrasenia: TextView): Boolean {
        val contrasenia = inputContrasenia.text.toString()
        val contraseniaValida = contrasenia.matches(Regex("^(?=.*[a-zA-Z])(?=.*\\d).{6,}\$"))
        return contraseniaValida
    }

    private fun confirmarContrasenias(
        inputContrasenia: TextView,
        inputConfirmarContrasenia: TextView
    ): Boolean {
        val contrasenia = inputContrasenia.text.toString()
        val confirmarContrasenia = inputConfirmarContrasenia.text.toString()
        return contrasenia == confirmarContrasenia
    }


}