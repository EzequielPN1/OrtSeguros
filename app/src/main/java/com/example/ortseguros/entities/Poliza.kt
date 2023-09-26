package com.example.ortseguros.entities

import java.util.Date

class Poliza (
    var id : String,
    var idUsuario: String,
    var marca : String,
    var modelo : String,
    var anio : Int,
    var patente : String,
    var numMotor : Int,
    var numChasis : Int,
    var vigenciaDesde : Date,
    var vigenciaHasta : Date,
    var respCivil : Boolean,
    var dañoTotal : Boolean,
    var granizo : Boolean,
    var roboHurtoTotal : Boolean,
    var valorDelVehiculo : Int,
    var sumaAsegurada : Int,
    var valorCuota : Int,

){

 }
