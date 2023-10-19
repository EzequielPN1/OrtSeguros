package com.example.ortseguros.entities

data class Usuario(
    var numCliente: String = "",
    var id: String = "",
    var nombre: String = "",
    var apellido: String = "",
    var fechaNac: String = "",
    var dni: String = "",
    var domicilio: String = "",
    var email: String = "",
    var telefono: String = "",
    var tarjetaDeCredito: TarjetaDeCredito = TarjetaDeCredito()
) {
    data class TarjetaDeCredito(
        var numeroDeTarjeta: String = "",
        var fechaCaducidad: String = "",
        var titular: String = "",
        var dni: String = "",
        var codigoSeguridad: String = "",
    )
}
