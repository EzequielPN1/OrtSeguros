package com.example.ortseguros.fragments.login

import android.widget.TextView
import androidx.lifecycle.ViewModel
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

class RegisterViewModel : ViewModel() {

    fun validarCampos(inputNombre: TextView,inputApellido: TextView,inputFechaNac: TextView,inputDni: TextView,inputDomicilio: TextView,
                      inputEmail: TextView, inputTelefono: TextView,inputContrasenia: TextView, inputConfirmarContrasenia: TextView): String? {

         if(validarIngresoVacio(inputNombre)){
             return "Por favor, ingrese su nombre."
         }else if(validarIngresoVacio(inputApellido)){
             return "Por favor, ingrese su apellido."
         }else  if(validarIngresoVacio(inputFechaNac)) {
             return "Por favor, ingrese su fecha de nacimiento."
         }else  if(validarEdad(inputFechaNac)) {
             return "Debe ser mayor de edad."
         }else if(validarIngresoVacio(inputDni)){
             return "Por favor, ingrese su dni."
         }else if(validarIngresoVacio(inputDomicilio)){
             return "Por favor, ingrese su domicilio."
         } else if (validarIngresoVacio(inputEmail)) {
            return "Por favor, ingrese su email."
        } else if(validarIngresoVacio(inputTelefono)){
            return "Por favor, ingrese su itelefono."
        }else if (!validarEmail(inputEmail)) {
            return "Por favor, ingrese un email correcto."
        } else if (validarIngresoVacio(inputContrasenia)) {
            return "Por favor, ingrese su contraseña."
        } else if (!validarContrasenia(inputContrasenia)) {
            return "Mínimo 6 caracteres, contenga al menos una letra y al menos un número."
        } else if (!confirmarContrasenias(inputContrasenia, inputConfirmarContrasenia)) {
            return "Error al validar, sus contraseñas deben coincidir."
        }

        return null
    }

    private fun validarEdad(inputFechaNac: TextView): Boolean {
        val fechaNacimientoStr = inputFechaNac.text.toString()

        // Formatea la fecha de nacimiento ingresada
        val dateFormat = DateFormat.getDateInstance()
        val fechaNacimiento: Date? = dateFormat.parse(fechaNacimientoStr)

        if (fechaNacimiento != null) {
            // Calcula la fecha actual
            val fechaActual = Calendar.getInstance().time

            // Calcula la diferencia en años entre la fecha de nacimiento y la fecha actual
            val diferenciaEnMillis = fechaActual.time - fechaNacimiento.time
            val edad = diferenciaEnMillis / (1000L * 60 * 60 * 24 * 365)

            return edad < 18
        } else {
            // Tratar el caso de fechaNacimiento nula o inválida
            return false
        }
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