package com.example.ortseguros.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Siniestro (
    var numSiniestro:String="",
    var id:String="",
    var idUsuario: String = "",
    var idPoliza: String = "",
    var fecha: String = "",
    var hora: String = "",
    var ubicacion: String = "",
    var descripcion: String = "",
    var patente: String = "",
    var tipoSiniestro: String = "",
    var finalizado: Boolean= false,
    var mensaje: String= "",
): Parcelable
