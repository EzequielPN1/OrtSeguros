package com.example.ortseguros.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Poliza (
    var id : String= "",
    var idUsuario: String= "",
    var marcaModelo : String= "",
    var anio : String= "",
    var patente : String= "",
    var fechaInicio : String= "",  // se pone de manera automatica al guardar la poliza
    var valorDelVehiculo : String= "",
    var sumaAsegurada : String= "", // se calcula autmaticamente
    var valorCuota : String= "",    // se calcula autmaticamente
    var respCivil : Boolean,
    var danioTotal : Boolean,
    var granizo : Boolean,
    var roboParcial : Boolean,
    var roboTotal : Boolean,

): Parcelable
