package com.example.ortseguros.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mensaje (
    var estado: Boolean = false,
    var fecha: String = "",
    var notificacion: String = "",
    var numero: String = "",
    var usuarioEmpresa: String = ""
): Parcelable
