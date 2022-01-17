package com.jefisu.animedemo.ui.theme.presentation.main

import com.jefisu.animedemo.data.dto.AnimeResponse

sealed class MainEvent {
    object GetAnime : MainEvent()
    object SaveAnime : MainEvent()
    data class DeleteAnime(val anime: AnimeResponse) : MainEvent()
    data class ChangeValueName(val name: String): MainEvent()
}
