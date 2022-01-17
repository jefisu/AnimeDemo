package com.jefisu.animedemo.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnimeResponse(
    val name: String,
    val id: String,
)