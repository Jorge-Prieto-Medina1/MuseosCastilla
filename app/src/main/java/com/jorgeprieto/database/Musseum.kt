package dev.jorgeprieto.videoclub.model

import java.io.Serializable

data class Movie (
    val id: Int,
    val name: String,
    val descripion: String,
    val cover: String,
) : Serializable