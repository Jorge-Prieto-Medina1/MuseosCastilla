package com.jorgeprieto.database

import java.io.Serializable

data class Museum(
    var id: String = "",
    var imagen: String = "",
    var latitud: Double = 0.0,
    var localidad: String = "",
    var longitud: Double = 0.0,
    var nombre: String = "",
    var puntuacion: Double = 0.0,
    var votos: Long = 0
) : Serializable

