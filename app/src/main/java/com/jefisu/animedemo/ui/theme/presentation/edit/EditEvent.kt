package com.jefisu.animedemo.ui.theme.presentation.edit

import androidx.compose.ui.focus.FocusState

sealed class EditEvent {
    object SaveAnime : EditEvent()
    data class EnteredName(val value: String) : EditEvent()
    data class ChangeNameFocus(val focusState: FocusState) : EditEvent()
}
