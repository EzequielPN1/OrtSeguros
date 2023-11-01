package com.example.ortseguros.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Poliza (
    var numPoliza:String="",
    var id : String="",
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
    var roboTotal: Boolean = false,
    var pagos: List<Pago> = mutableListOf(),
    var uriImagePredeterminada:String = "",
    var uriImageFrente:String = "",
    var uriImageLatDer:String = "",
    var uriImageLatIzq:String = "",
    var uriImagePosterior:String = "",
    var activa:Boolean = true,
    var eliminada:Boolean = false,
    var actualizada:Boolean = false,
) : Parcelable
