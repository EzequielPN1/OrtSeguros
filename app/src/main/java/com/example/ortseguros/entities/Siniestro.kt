package com.example.ortseguros.entities

import android.location.Location
import java.util.Date

class Siniestro (
    var id : String,
    var idUsuario: String,
    var idPoliza : String,
    var fecha : Date,
    var ubicacion : Location
)