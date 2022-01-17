package com.jefisu.animedemo.data.util

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
    object AddedDeletedAnime : UiEvent()
    object UpdatedAnime : UiEvent()
}
