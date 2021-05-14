package com.jorgeprieto.database

import java.io.Serializable

data class Imagen(
    var id: String = "",
    var userId: String ="",
    var museumId: String="",
) : Serializable
