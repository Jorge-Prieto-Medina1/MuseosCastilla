package com.jorgeprieto.database.data

import java.io.Serializable


data class Calification(
    var id: String = "",
    var nota: Double = 0.0,
    var userId: String ="",
    var museumId: String="",
) : Serializable
