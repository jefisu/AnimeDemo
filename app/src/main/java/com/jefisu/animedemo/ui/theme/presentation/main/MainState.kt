package com.jefisu.animedemo.ui.theme.presentation.main

import com.jefisu.animedemo.data.dto.AnimeResponse

data class MainState(
    val animes: List<AnimeResponse> = emptyList(),
    val isLoading: Boolean = false
)
