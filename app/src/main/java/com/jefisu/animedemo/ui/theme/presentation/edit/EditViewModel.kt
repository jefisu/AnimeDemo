package com.jefisu.animedemo.ui.theme.presentation.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.animedemo.data.dto.AnimeResponse
import com.jefisu.animedemo.data.dto.toFormat
import com.jefisu.animedemo.data.repository.AnimeRepository
import com.jefisu.animedemo.data.util.Resource
import com.jefisu.animedemo.data.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var name by mutableStateOf(TextFieldState())
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
                _currentAnime?.let { anime ->
                    name = name.copy(
                        text = anime.name
                    )
                }
            }
        }
    }

    fun onEvent(event: EditEvent) {
        when (event) {
            is EditEvent.SaveNewData -> {
                viewModelScope.launch {
                    val result = _currentAnime?.let {
                        repository.updateAnime(
                            AnimeResponse(
                                name = name.text,
                                timestamp = System.currentTimeMillis().toFormat("HH:mm"),
                                date = System.currentTimeMillis().toFormat("MM/dd/yyyy"),
                                id = it.id
                            )
                        )
                    }
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.UpdatedAnime)
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    message = result.uiText ?: "Erro ao atualizar"
                                )
                            )
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
}