package com.example.ortseguros.entities

import java.util.Date

class PolizaRepository () {

    var polizas : MutableList<Poliza> = mutableListOf()

    init{
        polizas.add(Poliza("1","1","Peugeot","207",2017,"ABC123",123,123, Date(2023,8,10), Date(2023,9,10),true,true,true,true,2000,3000,4000))
        polizas.add(Poliza("2","2","Citroen","C3",2015,"LJJ123",123,123, Date(2023,8,10), Date(2023,9,10),true,true,true,true,2000,3000,4000))
        polizas.add(Poliza("3","3","Ford","K",2010,"PJN234",123,123, Date(2023,8,10), Date(2023,9,10),true,true,true,true,2000,3000,4000))
        polizas.add(Poliza("4","4","Yamaha","FZ",2015,"A115XMM",123,123, Date(2023,8,10), Date(2023,9,10),true,true,true,true,2000,3000,4000))
    }
}