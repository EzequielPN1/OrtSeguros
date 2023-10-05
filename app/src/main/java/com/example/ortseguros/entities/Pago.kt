package com.example.ortseguros.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Pago(
    var numeroPago:String= "",
    var fechaVencimiento: String = "",
    var fechaPago: String = "",
    var abonado: Boolean = false,
): Parcelable
