package com.example.ortseguros.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Siniestro (
    var idUsuario: String = "",
    var idPoliza: String = "",
    var fecha: String = "",
    var hora: String = "",
    var ubicacion: String = "",
    var descripcion: String = "",
    var patente: String = "",
    var tipoSiniestro: String = "",
): Parcelable
