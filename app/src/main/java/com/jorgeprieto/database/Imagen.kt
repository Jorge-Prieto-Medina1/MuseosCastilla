package com.jorgeprieto.database

import java.io.Serializable

//Objeto serializable para la imagen
data class Imagen(
    var id: String = "",
    var userId: String ="",
    var museumId: String="",
) : Serializable
