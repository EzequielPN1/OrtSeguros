package com.example.ortseguros.entities

import java.util.Date

class PolizaRepository () {

    var polizas : MutableList<Poliza> = mutableListOf()

    init{
        polizas.add(Poliza("1","1","Peugeot","207",2017,"ABC123",123,123, "2023,9,10", "2023,9,10",true,true,true,true,true,30,30,3))

    }
}