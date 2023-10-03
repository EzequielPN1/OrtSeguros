package com.example.ortseguros.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Poliza (
    var idUsuario: String = "",
    var marcaModelo: String = "",
    var fechaAltaVehiculo: String = "",
    var patente: String = "",
    var fechaInicioPoliza: String = "",
    var sumaAsegurada: String = "",
    var valorCuota: String = "",
    var respCivil: Boolean = false,
    var danioTotal: Boolean = false,
    var granizo: Boolean = false,
    var roboParcial: Boolean = false,
    var roboTotal: Boolean = false
) : Parcelable
