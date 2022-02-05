package com.jefisu.animedemo.ui.theme.presentation.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.animedemo.data.dto.AnimePost
import com.jefisu.animedemo.data.dto.AnimeResponse
import com.jefisu.animedemo.data.dto.toFormat
import com.jefisu.animedemo.data.repository.AnimeRepository
import com.jefisu.animedemo.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var name by mutableStateOf(TextFieldState(hint = "Enter title"))
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _currentAnime: AnimeResponse? = null

    init {
        val animeId = savedStateHandle.get<String>("id")
        animeId?.let { id ->
            viewModelScope.launch {
                val anime = repository.getAnimeById(id)
                _currentAnime = anime
                _currentAnime?.let {
                    name = name.copy(
                        text = it.name,
                        isHintVisible = false
                    )
                }
            }
        }
    }

    fun onEvent(event: EditEvent) {
        when (event) {
            is EditEvent.SaveAnime -> {
                viewModelScope.launch {
                    val anime = _currentAnime
                    if (anime != null) {
                        _currentAnime?.let {
                            val result = repository.insertAnime(
                                newAnime = null,
                                updateAnime = AnimeResponse(
                                    name = name.text,
                                    timestamp = it.timestamp,
                                    date = it.date,
                                    lastModify = System.currentTimeMillis()
                                        .toFormat("MM/dd/yyyy - HH/mm"),
                                    id = it.id
                                )
                            )
                            when (result) {
                                is Resource.Success -> {
                                    _eventFlow.emit(UiEvent.AddUpdateAnime)
                                }
                                is Resource.Error -> {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackBar(
                                            message = result.uiText ?: "Erro ao inserir"
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        if (name.text.isBlank()) {
                            _eventFlow.emit(UiEvent.ShowSnackBar("Preechimento obrigatório do campo"))
                        } else {
                            val result = repository.insertAnime(
                                newAnime = AnimePost(
                                    name = name.text,
                                    timestamp =  System.currentTimeMillis().toFormat("HH:mm"),
                                    date =  System.currentTimeMillis().toFormat("MM/dd/yyyy")
                                ),
                                updateAnime = null
                            )
                            when (result) {
                                is Resource.Success -> {
                                    _eventFlow.emit(UiEvent.AddUpdateAnime)
                                }
                                is Resource.Error -> {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackBar(
                                            message = result.uiText ?: "Erro ao inserir"
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            is EditEvent.EnteredName -> {
                name = name.copy(text = event.value)
            }
            is EditEvent.ChangeNameFocus -> {
                name = name.copy(
                    isHintVisible = !event.focusState.isFocused && name.text.isBlank()
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object AddUpdateAnime: UiEvent()
    }
}