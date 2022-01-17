package com.jefisu.animedemo.ui.theme.presentation.main

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.animedemo.data.dto.AnimePost
import com.jefisu.animedemo.data.repository.AnimeRepository
import com.jefisu.animedemo.data.util.Resource
import com.jefisu.animedemo.data.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    var name by mutableStateOf("")
        private set

    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        onEvent(MainEvent.GetAnime)
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.GetAnime -> {
                viewModelScope.launch {
                   repository.getAnimes()
                       .onEach { event ->
                           when(event) {
                               is Resource.Success -> {
                                   _state.value = state.value.copy(
                                       animes = event.data ?: emptyList(),
                                       isLoading = false
                                   )
                               }
                               is Resource.Error -> {
                                   _eventFlow.emit(
                                       UiEvent.ShowSnackBar(
                                           message = event.uiText ?: "Erro desconhecido"
                                       )
                                   )
                               }
                           }
                       }.launchIn(this)
                }
            }
            is MainEvent.SaveAnime -> {
                viewModelScope.launch {
                    if (name.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowSnackBar("Preechimento obrigatório do campo"))
                    } else {
                        val result = repository.insertAnime(AnimePost(name))
                        when (result) {
                            is Resource.Success -> {
                                _eventFlow.emit(UiEvent.AddedDeletedAnime)
                            }
                            is Resource.Error -> {
                                _eventFlow.emit(
                                    UiEvent.ShowSnackBar(
                                        result.uiText ?: "Erro desconhecido"
                                    )
                                )
                            }
                        }
                        name = ""
                    }
                }
            }
            is MainEvent.DeleteAnime -> {
                viewModelScope.launch {
                    val result = repository.deleteAnime(event.anime)
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.AddedDeletedAnime)
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    result.uiText ?: "Erro desconhecido"
                                )
                            )
                        }
                    }
                }
            }
            is MainEvent.ChangeValueName -> name = event.name
        }
    }
}